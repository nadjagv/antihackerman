import {
  TextField,
  Box,
  Stack,
  Typography,
  Button,
  Checkbox,
} from "@mui/material";
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
  const [boolAlarm, setBoolAlarm] = useState(false);
  const [borderMin, setBorderMin] = useState("");
  const [borderMax, setBorderMax] = useState("");
  const [activationCount, setActivationCount] = useState("");

  axios.defaults.withCredentials = true;

  const newAlarm = () => {
    let alarm = {
      name: alarmName,
      alarmForBool: boolAlarm,
      borderMin: borderMin,
      borderMax: borderMax,
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
      <Box>
        <Box
          component="form"
          sx={{
            "& .MuiTextField-root": { m: 1, width: "25ch" },
          }}
          noValidate
          autoComplete="off"
        >
          <Stack
            justifyContent="center"
            alignItems="center"
            spacing={2}
            marginTop={2}
          >
            <TextField
              id="outlined-alarm-name-input"
              label="Alarm name"
              type="text"
              onChange={(e) => setAlarmName(e.target.value)}
              value={alarmName}
            />
            {type === "digital" ? (
              <TextField
                id="outlined-border-min-input"
                label="Border min"
                type="number"
                onChange={(e) => setBorderMin(e.target.value)}
                value={borderMin}
              />
            ) : (
              <Stack
                justifyContent="center"
                alignItems="center"
                spacing={2}
                marginTop={2}
                direction="row"
              >
                <Typography>On activation</Typography>
                <Checkbox
                  //checked={boolAlarm}
                  value={boolAlarm}
                  label="On activation"
                  onChange={(e) => setBoolAlarm(e.target.value)}
                  inputProps={{ "aria-label": "controlled" }}
                />
              </Stack>
            )}

            {type === "digital" ? (
              <TextField
                id="outlined-border-max-input"
                label="Border max"
                type="number"
                onChange={(e) => setBorderMax(e.target.value)}
                value={borderMax}
              />
            ) : (
              ""
            )}
            <Button
              onClick={() => {
                newAlarm();
              }}
              variant="contained"
              color="success"
            >
              Create alarm
            </Button>
          </Stack>
        </Box>
      </Box>
    </div>
  );
}
export default NewAlarm;
