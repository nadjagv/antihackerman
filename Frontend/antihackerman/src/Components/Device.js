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
import React from "react";
import { useState, useEffect } from "react";
import axios from "axios";
import { useParams, useNavigate } from "react-router-dom";
import Header from "./Header";
import AuthService from "../Services/AuthService";
import environment from "../Constants/Environment";

function Device() {
  const navigation = useNavigate();
  const { id } = useParams();
  const [alarms, setAlarms] = useState([]);
  const [digital, setDigital] = useState(true);

  useEffect(() => {
    axios.get(environment.baseURL + "devices/" + id).then((response) => {
      setDigital(response.data.type === "INTERVAL_DEVICE" ? true : false);
    });
    setDigital(true);

    axios
      .get(environment.baseURL + "device-alarms/device/" + id)
      .then((response) => {
        setAlarms(response.data);
      });
  }, []);
  axios.defaults.withCredentials = true;

  const handleDeleteAlarm = (alarmId) => {
    axios
      .delete(environment.baseURL + "device-alarms/" + alarmId)
      .then((response) => {
        alert("alarm deleted");
      });
    //window.location.reload();
  };
  useEffect(() => {}, []);
  return (
    <div>
      <Header user={AuthService.getUser()}></Header>
      <Stack>
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Name</TableCell>
                {digital ? (
                  <TableCell>Digital minimum</TableCell>
                ) : (
                  <TableCell>Analog activation</TableCell>
                )}
                {digital ? <TableCell>Digital maximum</TableCell> : ""}
                <TableCell>Delete alarm</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {alarms.map((a) => (
                <TableRow>
                  <TableCell>{a.name}</TableCell>
                  {digital ? (
                    <TableCell>{a.borderMin}</TableCell>
                  ) : (
                    <TableCell>{a.alarmForBool ? "1" : "0"}</TableCell>
                  )}
                  {digital ? <TableCell>{a.borderMax}</TableCell> : ""}
                  <TableCell>
                    <Button
                      onClick={() => {
                        handleDeleteAlarm(a.id);
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
        <Button
          onClick={() => {
            navigation(
              "/device/newAlarm/" + id + "/" + (digital ? "digital" : "analog")
            );
          }}
          variant="contained"
          color="success"
        >
          New alarm
        </Button>
      </Stack>
    </div>
  );
}
export default Device;
