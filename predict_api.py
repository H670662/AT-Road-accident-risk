from fastapi import FastAPI
from pydantic import BaseModel
import pandas as pd
import joblib

model = joblib.load("models/road_risk_lgbm_stacked.joblib")

# Handles the RandomForest params (I used it previously for RandomForest only)
class InputData(BaseModel):
    road_type: str = "urban"
    speed_limit: float = 50
    lighting: str = "daylight"
    weather: str = "clear"
    time_of_day: str = "night"
    num_lanes: int = 2
    curvature: float = 0
    num_reported_accidents: int = 0
    road_signs_present: bool = True
    public_road: bool = True
    holiday: bool = False
    school_season: bool = False


# Handles the additional params in the stacked model
def create_engineered_features(df: pd.DataFrame) -> pd.DataFrame:
    df = df.copy()

    df["speed_o_curve"] = df["speed_limit"] / (df["curvature"] + 1e-6)
    df["speed_o_curve"] = df["speed_o_curve"].fillna(0)

    lighting_w = {"night": 0.9, "dim": 0.3, "daylight": 0.1}
    weather_w = {"foggy": 0.8, "rainy": 0.7, "clear": 0.1}
    df["lighting_risk"] = df["lighting"].map(lighting_w).fillna(0)
    df["weather_risk"] = df["weather"].map(weather_w).fillna(0)
    df["visibility_composite"] = (df["lighting_risk"] + df["weather_risk"]) / 2

    time_order = {"morning": 1, "evening": 2, "afternoon": 3}
    df["time_as_int"] = df["time_of_day"].map(time_order)

    return df


app = FastAPI(Title="Road Accident Risk Predictor")

@app.post("/predict")
def predict(data: InputData):
    df = pd.DataFrame([data.model_dump()])

    df = create_engineered_features(df)

    prediction = model.predict(df)[0]

    return {"risk": float(prediction)}