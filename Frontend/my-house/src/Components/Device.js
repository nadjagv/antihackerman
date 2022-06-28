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
  const [deviceMessages, setDeviceMessages] = useState([]);

  axios.defaults.withCredentials = true;

  useEffect(() => {
    axios
      .get(environment.baseURL + "devices/messages/" + id)
      .then((response) => {
        setDeviceMessages(response.data);
      });
  }, []);
  return (
    <div>
      <Header user={AuthService.getUser()}></Header>
      <Stack>
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Message</TableCell>
                <TableCell>Value</TableCell>
                <TableCell>Timestamp</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {deviceMessages.map((m) => (
                <TableRow>
                  <TableCell>{m.message}</TableCell>
                  <TableCell>{m.value}</TableCell>
                  <TableCell>{m.timestamp}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </Stack>
    </div>
  );
}
export default Device;
