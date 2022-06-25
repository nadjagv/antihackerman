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
  { name: "Grbavica", id: 1 },
  { name: "Detelinara", id: 2 },
];

function Group() {
  const navigation = useNavigate();
  const { id } = useParams();
  const [objects, setObjects] = useState([]);

  axios.defaults.withCredentials = true;

  useEffect(() => {
    axios
      .get(
        environment.baseURL +
          "real-estates/" +
          id +
          "/" +
          AuthService.getUser().username
      )
      .then((response) => {
        setObjects(response.data);
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
                <TableCell>Location</TableCell>
                <TableCell>View devices</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {objects.map((o) => (
                <TableRow key={o.id}>
                  <TableCell>{o.name}</TableCell>
                  <TableCell>{o.location}</TableCell>
                  <TableCell>
                    <Button
                      onClick={() => {
                        navigation("/object/" + o.id);
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
export default Group;
