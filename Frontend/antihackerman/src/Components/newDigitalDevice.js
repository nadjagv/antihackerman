import { TextField, Box, Stack, Typography, Button } from "@mui/material";
import React from "react";
import { useState } from "react";
import environment from "../Constants/Environment";
import axios from "axios";
import Header from "./Header";
import AuthService from "../Services/AuthService";
import { useParams } from "react-router-dom";

function NewDigitalDevice() {
  const { id } = useParams();

  const [deviceName, setDeviceName] = useState("");
  const [interval, setInterval] = useState("");
  const [filepath, setFilePath] = useState("");
  const [filter, setFilter] = useState("");
  const [valueDefinition, setValueDefinition] = useState("");
  const [minValue, setMinValue] = useState("");
  const [maxValue, setMaxValue] = useState("");

  axios.defaults.withCredentials = true;

  const newDevice = () => {
    if (deviceName === "" || filepath === "" || valueDefinition === "") {
      alert("Empty fields");
      return;
    }
    let validFilter = new RegExp("^([=><][1-9][0-9]*)?$");
    if (!validFilter.test(filter)) {
      alert("Invalid filter field");
      return;
    }

    let validFilePath = new RegExp("^[\\w,\\s-]+\\.json$");
    if (!validFilePath.test(filepath)) {
      alert("Invalid filepath");
      return;
    }

    let Device = {
      name: deviceName,
      filePath: filepath,
      filter: filter,
      readIntervalMils: interval,
      realestateId: id,
      valueDefinition: valueDefinition,
      type: "INTERVAL_DEVICE",
      maxValue: maxValue,
      minValue: minValue,
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
          <Typography variant="h6">Create new Digital Device</Typography>
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
              type="number"
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
              id="outlined-value-definition-input"
              label="Value definition"
              type="text"
              onChange={(e) => setValueDefinition(e.target.value)}
              value={valueDefinition}
            />
            <TextField
              id="outlined-min-value-input"
              label="Minimal value"
              type="number"
              onChange={(e) => setMinValue(e.target.value)}
              value={minValue}
            />
            <TextField
              id="outlined-max-value-input"
              label="Maximum value"
              type="number"
              onChange={(e) => setMaxValue(e.target.value)}
              value={maxValue}
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
export default NewDigitalDevice;
