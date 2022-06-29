import React from "react";
import Header from "./Header";
import AuthService from "../Services/AuthService";
import { useState, useEffect } from "react";
import axios from "axios";
import environment from "../Constants/Environment";
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

export default function NewAdminAlarm() {
  const [type, setType] = useState("INFO");
  const [name, setName] = useState("");
  const [user, setUser] = useState("");
  const [seq, setSeq] = useState("");
  const [alarms, setAlarms] = useState([]);

  axios.defaults.withCredentials = true;

  useEffect(() => {
    axios.get(environment.baseURL + "log-alarms").then((response) => {
      console.log(response.data);
      setAlarms(response.data);
    });
  }, []);

  const addAlarm = () => {
    if (!name) {
      alert("Name can't be empty!");
    } else {
      if (!user && !type && !seq) {
        alert("At least one field needs to be filled!");
      } else {
        let alarm={
            name: name,
            username: user,
            charSequence: seq,
            logType: type,
        }
        axios.post(environment.baseURL + "log-alarms",alarm).then((response) => {
            setAlarms([...alarms,response.data])
          });
      }
    }
  };

  const deleteAlarm=(id)=>{
    axios.delete(environment.baseURL + "log-alarms/"+id).then((response) => {
        console.log(response.data);
        const newAlarms=alarms.filter(a=>a.id!=id)
        setAlarms(newAlarms)
      });
  }

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
            id="outlined-name-input"
            label="Alarm name"
            type="text"
            onChange={(e) => {
              setName(e.target.value);
            }}
            value={name}
          />
          <Autocomplete
            disablePortal
            id="combo-box-types"
            options={types}
            sx={{ width: 300 }}
            renderInput={(params) => <TextField {...params} label="Type" />}
            value={type}
            onChange={(e, newValue) => {
              setType(newValue);
            }}
          />
          <TextField
            id="outlined-user-input"
            label="User"
            type="text"
            onChange={(e) => {
              setUser(e.target.value);
            }}
            value={user}
          />
          <TextField
            id="outlined-sequence-input"
            label="Sequence"
            type="text"
            onChange={(e) => {
              setSeq(e.target.value);
            }}
            value={seq}
          />
          <Button
            onClick={() => {
              addAlarm();
            }}
            variant="contained"
            color="success"
          >
            Add
          </Button>
        </Stack>
      </Stack>
      <TableContainer>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Name</TableCell>
              <TableCell>Log Type</TableCell>
              <TableCell>User</TableCell>
              <TableCell>Sequence</TableCell>
              <TableCell></TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {alarms.map((a) => (
              <TableRow key={a.id}>
                <TableCell>{a.name}</TableCell>
                <TableCell>{a.logType}</TableCell>
                <TableCell>{a.username}</TableCell>
                <TableCell>{a.charSequence}</TableCell>
                <TableCell>
                  <Button
                    onClick={() => {
                      deleteAlarm(a.id);
                    }}
                    variant="contained"
                    color="error"
                  >
                    Delete
                  </Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </div>
  );
}
