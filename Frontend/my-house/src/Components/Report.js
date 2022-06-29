import React from "react";
import Header from "./Header";
import { useState, useEffect } from "react";
import environment from "../Constants/Environment";
import axios from "axios";
import AuthService from "../Services/AuthService";
import {
  TableContainer,
  TableHead,
  Table,
  TableRow,
  TableCell,
  TableBody,
  Button,
  Stack,
  Modal,
  Box,
  Typography,
  Autocomplete,
  TextField,
} from "@mui/material";
import moment from "moment";

export default function Report() {
  const [devices, setDevices] = useState([]);
  const [start, setStart] = useState(null);
  const [end, setEnd] = useState(null);
  const [doSearch, setDoSearch] = useState(true);

  axios.defaults.withCredentials = true;

  useEffect(() => {
    let params = {
      start: start
        ? moment(new Date(start)).format("DD/MM/YYYY HH:mm:ss")
        : null,
      end: end ? moment(new Date(end)).format("DD/MM/YYYY HH:mm:ss") : null,
    };
    axios.get(environment.baseURL + "devices/report", { params }).then((response) => {
        console.log(response.data);
        setDevices(response.data);
      });
  }, [doSearch]);

  const search = () => {
    setDoSearch(!doSearch);
  };

  return (
    <div>
      <Header user={AuthService.getUser()}></Header>
      <Stack
        justifyContent="center"
        alignItems="center"
        spacing={2}
        marginTop={2}
      >
        <Stack
          direction="row"
          justifyContent="center"
          alignItems="center"
          spacing={2}
          marginTop={2}
        >
          <TextField
            id="datetime-local-start"
            label="Start date and time"
            type="datetime-local"
            sx={{ width: 250 }}
            InputLabelProps={{
              shrink: true,
            }}
            value={start}
            onChange={(e, newValue) => {
              setStart(e.target.value);
              search();
            }}
          />
          <TextField
            id="datetime-local-end"
            label="End date and time"
            type="datetime-local"
            sx={{ width: 250 }}
            InputLabelProps={{
              shrink: true,
            }}
            value={end}
            onChange={(e, newValue) => {
              setEnd(e.target.value);
              search();
            }}
          />
        </Stack>
      </Stack>
      <TableContainer>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Device</TableCell>
              <TableCell>Real Estate</TableCell>
              <TableCell>Group</TableCell>
              <TableCell>Number of Alarms</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {devices.map((d) => (
              <TableRow key={d.deviceId}>
                <TableCell>{d.deviceName}</TableCell>
                <TableCell>{d.realEstateName}</TableCell>
                <TableCell>{d.groupName}</TableCell>
                <TableCell>{d.numberOfAlarms}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </div>
  );
}
