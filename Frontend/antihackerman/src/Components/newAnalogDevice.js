import { TextField, Box, Stack, Typography, Button } from "@mui/material";
import React from "react";
import { useState } from "react";
import environment from "../Constants/Environment";
import axios from "axios";
import Header from "./Header";
import AuthService from "../Services/AuthService";
import { useParams } from "react-router-dom";

function NewAnalogDevice() {
  const { id } = useParams();

  const [deviceName, setDeviceName] = useState("");
  const [interval, setInterval] = useState("");
  const [filepath, setFilePath] = useState("");
  const [filter, setFilter] = useState("");
  const [zeroValue, setZeroValue] = useState("");
  const [oneValue, setOneValue] = useState("");

  axios.defaults.withCredentials = true;

  const newDevice = () => {
    let Device = {
      name: deviceName,
      filePath: filepath,
      filter: filter,
      readIntervalMils: interval,
      activeTrueStr: oneValue,
      activeFalseStr: zeroValue,
      realestateId: id,
      type: "BOOLEAN_DEVICE",
    };
    axios.post(environment.baseURL + "devices", Device).then((response) => {
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
          <Typography variant="h6">Create new Analog Device</Typography>
          <Stack spacing="3" alignItems="center">
            <TextField
              id="outlined-device-name-input"
              label="Device name"
              type="text"
              onChange={(e) => setDeviceName(e.target.value)}
              value={deviceName}
            />
            <TextField
              id="outlined-interval-input"
              label="Interval in milis"
              type="text"
              onChange={(e) => setInterval(e.target.value)}
              value={interval}
            />
            <TextField
              id="outlined-filepath-input"
              label="Filepath"
              type="text"
              onChange={(e) => setFilePath(e.target.value)}
              value={filepath}
            />
            <TextField
              id="outlined-filter-input"
              label="Filter"
              type="text"
              onChange={(e) => setFilter(e.target.value)}
              value={filter}
            />
            <TextField
              id="outlined-zero-value-input"
              label="Zero value"
              type="text"
              onChange={(e) => setZeroValue(e.target.value)}
              value={zeroValue}
            />
            <TextField
              id="outlined-one-value-input"
              label="One value"
              type="text"
              onChange={(e) => setOneValue(e.target.value)}
              value={oneValue}
            />
            <Button
              onClick={() => {
                newDevice();
              }}
              variant="contained"
              color="success"
            >
              Create device
            </Button>
          </Stack>
        </Box>
      </Box>
    </div>
  );
}
export default NewAnalogDevice;
