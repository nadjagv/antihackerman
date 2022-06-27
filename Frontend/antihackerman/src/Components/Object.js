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
  const [devices, setDevices] = useState(testObjects);

  axios.defaults.withCredentials = true;

  useEffect(() => {
    axios.get(environment.baseURL + "real-estates/" + id).then((response) => {
      setDevices(response.data.devices);
    });
  }, []);

  const handleDeleteDevice = (deviceId) => {
    axios
      .delete(environment.baseURL + "devices/" + deviceId)
      .then((response) => {
        alert("Device deleted");
      });
    //window.location.reload();
  };
  return (
    <div>
      <Header user={AuthService.getUser()}></Header>
      <Stack>
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Name</TableCell>
                <TableCell>View device</TableCell>
                <TableCell>Delete device</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {devices.map((d) => (
                <TableRow key={d.id}>
                  <TableCell>{d.name}</TableCell>
                  <TableCell>
                    <Button
                      onClick={() => {
                        //navigation("/device/" + d.id);
                      }}
                      variant="contained"
                      color="success"
                    >
                      View
                    </Button>
                  </TableCell>
                  <TableCell>
                    <Button
                      onClick={() => {
                        handleDeleteDevice(d.id);
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
        <Stack
          marginTop={10}
          direction="row"
          justifyContent="center"
          alignItems="center"
          spacing={2}
        >
          <Box>
            <Button
              onClick={() => {
                navigation("/object/" + id + "/newDigitalDevice");
              }}
              variant="contained"
              color="success"
            >
              Create digital device
            </Button>
          </Box>
          <Box marginTop={5} marginBottom={5}>
            <Button
              onClick={() => {
                navigation("/object/" + id + "/newAnalogDevice");
              }}
              variant="contained"
              color="success"
            >
              Create analog device
            </Button>
          </Box>
        </Stack>
      </Stack>
    </div>
  );
}
export default Object;
