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
import environment from "../Constants/Environment";
import modalStyle from "../Constants/Styles";
import EditUser from "./EditUser";
import { useParams } from "react-router-dom";

const usersTest = [
  { username: "Ksnaga", email: "hajduk.dusan@gmail.com", role: "owner" },
];
const testObjects = [
  { name: "Grbavica", id: 1 },
  { name: "Detelinara", id: 2 },
];

function Group() {
  const { id } = useParams();
  const [users, setUsers] = useState(usersTest);
  const [objects, setObjects] = useState(testObjects);
  const [editUserModal, setEditUserModal] = useState(false);
  const [userToEdit, setUserToEdit] = useState("");
  const [userToDelete, setUserToDelete] = useState("");

  useEffect(() => {
    axios.get(environment.baseURL + "").then((response) => {
      setUsers(response.data);
    });
    axios.get(environment.baseURL + "").then((response) => {
      setUsers(response.data);
    });
  }, []);
  const editUserHandle = (user) => {
    setUserToEdit(user);
    setEditUserModal(true);
  };
  const deleteUser = (username) => {};
  return (
    <div>
      <Stack>
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Username</TableCell>
                <TableCell>Mail</TableCell>
                <TableCell>Role</TableCell>
                <TableCell>Edit user</TableCell>
                <TableCell>Delete user</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {users.map((user) => (
                <TableRow key={user.username}>
                  <TableCell>{user.username}</TableCell>
                  <TableCell>{user.email}</TableCell>
                  <TableCell>{user.role}</TableCell>
                  <TableCell>
                    <Button
                      onClick={() => {
                        editUserHandle(user);
                      }}
                      variant="contained"
                    >
                      Edit user
                    </Button>
                  </TableCell>
                  <TableCell>
                    <Button
                      onClick={() => {
                        deleteUser(user.username);
                      }}
                      variant="contained"
                      color="error"
                    >
                      Delete user
                    </Button>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
        <Box marginTop={5} marginBottom={5}>
          <Button variant="contained" color="success">
            Napravi korisnika
          </Button>
          <Button variant="contained" color="success">
            Napravi objekat
          </Button>
        </Box>
        <TableContainer>
          <Table>
            <TableHead>
              <TableCell>Naziv objekta</TableCell>
              <TableCell>ID</TableCell>
            </TableHead>
            <TableBody></TableBody>
            {objects.map((o) => (
              <TableRow>
                <TableCell>{o.name}</TableCell>
                <TableCell>{o.id}</TableCell>
              </TableRow>
            ))}
          </Table>
        </TableContainer>
      </Stack>
      <EditUser
        user={userToEdit}
        modal={editUserModal}
        close={setEditUserModal}
      ></EditUser>
    </div>
  );
}
export default Group;
