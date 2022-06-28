import { TextField, Box, Stack, Typography, Button } from "@mui/material";
import React from "react";
import { useState } from "react";
import environment from "../Constants/Environment";
import axios from "axios";
import Header from "./Header";
import AuthService from "../Services/AuthService";
import { useParams } from "react-router-dom";

function NewAlarm() {
  const { id, type } = useParams();

  const [alarmName, setAlarmName] = useState("");

  axios.defaults.withCredentials = true;

  const newAlarm = () => {
    let alarm = {
      deviceId: id,
    };
    axios
      .post(environment.baseURL + "device-alarms", alarm)
      .then((response) => {
        console.log("success");
      });
  };
  return (
    <div>
      <Header user={AuthService.getUser()}></Header>
    </div>
  );
}
export default NewAlarm;
