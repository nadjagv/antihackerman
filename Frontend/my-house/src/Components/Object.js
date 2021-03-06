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

const testObjects = [
  { name: "rerna", id: 1 },
  { name: "cistac vazduha", id: 2 },
];

function Object() {
  const navigation = useNavigate();
  const { id } = useParams();
  const [devices, setDevices] = useState([]);

  axios.defaults.withCredentials = true;

  useEffect(() => {
    axios.get(environment.baseURL + "real-estates/" + id).then((response) => {
      setDevices(response.data.devices);
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
                <TableCell>Name</TableCell>
                <TableCell>Type</TableCell>
                <TableCell>View device</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {devices.map((d) => (
                <TableRow key={d.id}>
                  <TableCell>{d.name}</TableCell>
                  <TableCell>
                    {d.type === "INTERVAL_DEVICE" ? "Digital" : "Analog"}
                  </TableCell>
                  <TableCell>
                    <Button
                      onClick={() => {
                        navigation("/device/" + d.id);
                      }}
                      variant="contained"
                      color="success"
                    >
                      View
                    </Button>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </Stack>
    </div>
  );
}
export default Object;
