import React from "react";
import Header from "./Header";
import AuthService from "../Services/AuthService";
import { useState, useEffect } from "react";
import axios from "axios";
import environment from "../Constants/Environment";
import moment from "moment";
import Moment from "react-moment";
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

const types = ["INFO", "SUCCESS", "WARNING", "ERROR"];

export default function Logs() {
  const [logs, setLogs] = useState([]);
  const [type, setType] = useState("");
  const [start, setStart] = useState(null);
  const [end, setEnd] = useState(null);
  const [user, setUser] = useState("");
  const [desc, setDesc] = useState("");
  const [doSearch, setDoSearch] = useState(true);

  axios.defaults.withCredentials = true;

  useEffect(() => {
    console.log(start);
    let params = {
      type: type === "" ? null : type,
      start: start
        ? moment(new Date(start)).format("DD/MM/YYYY HH:mm:ss")
        : null,
      end: end ? moment(new Date(end)).format("DD/MM/YYYY HH:mm:ss") : null,
      user: user === "" ? null : user,
      desc: desc === "" ? null : desc,
    };
    console.log(params);
    axios.get(environment.baseURL + "logs", { params }).then((response) => {
      setLogs(response.data);
      console.log(response.data);
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
          <Autocomplete
            disablePortal
            id="combo-box-types"
            options={types}
            sx={{ width: 300 }}
            renderInput={(params) => <TextField {...params} label="Type" />}
            value={type}
            onChange={(e, newValue) => {
              setType(newValue);
              search();
            }}
          />
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
          <TextField
            id="outlined-user-input"
            label="User"
            type="text"
            onChange={(e) => {
              setUser(e.target.value);
              search();
            }}
            value={user}
          />
          <TextField
            id="outlined-desc-input"
            label="Description"
            type="text"
            onChange={(e) => {
              setDesc(e.target.value);
              search();
            }}
            value={desc}
          />
        </Stack>
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Description</TableCell>
                <TableCell>IP</TableCell>
                <TableCell>Timestamp</TableCell>
                <TableCell>Type</TableCell>
                <TableCell>User</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {logs.map((l) => (
                <TableRow>
                  <TableCell>{l.desc}</TableCell>
                  <TableCell>{l.ip}</TableCell>
                  <TableCell>{l.timestamp}</TableCell>
                  <TableCell>{l.type}</TableCell>
                  <TableCell>{l.username}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </Stack>
    </div>
  );
}
