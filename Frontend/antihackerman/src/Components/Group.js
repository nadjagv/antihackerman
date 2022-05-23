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
import NewObject from "./NewObject";
import NewUser from "./NewUser";

const usersTest = [
  { username: "Ksnaga", email: "hajduk.dusan@gmail.com", role: "owner" },
];
const testObjects = [
  { name: "Grbavica", id: 1 },
  { name: "Detelinara", id: 2 },
];

function Group() {
  const { id } = useParams();
  const [users, setUsers] = useState([]);
  const [objects, setObjects] = useState([]);
  const [editUserModal, setEditUserModal] = useState(false);
  const [newObjectModal, setNewObjectModal] = useState(false);
  const [newUserModal, setNewUserModal] = useState(false);
  const [userToEdit, setUserToEdit] = useState("");
  const [userToDelete, setUserToDelete] = useState("");

  useEffect(() => {
    axios
      .get(environment.baseURL + "groups/all-users/" + id)
      .then((response) => {
        setUsers(response.data);
      });
    axios
      .get(environment.baseURL + "groups/real-estates/" + id)
      .then((response) => {
        setObjects(response.data);
      });
  }, []);

  const handleRemoveObject = (objectId) => {
    //axios.delete(environment.baseURL+"groups")
  };

  const handleNewObjectModal = () => {
    setNewObjectModal(true);
  };

  const handleNewUserModal = () => {
    setNewUserModal(true);
  };

  const editUserHandle = (user) => {
    setUserToEdit(user);
    setEditUserModal(true);
  };
  const deleteUser = (id) => {
    axios.delete(environment.baseURL + "users/" + id).then((response) => {
      alert("User deleted");
    });
  };
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
                        deleteUser(user.id);
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
          <Button
            variant="contained"
            color="success"
            onClick={() => {
              handleNewUserModal();
            }}
          >
            Create user
          </Button>
          <Button
            onClick={() => {
              handleNewObjectModal();
            }}
            variant="contained"
            color="success"
          >
            Create object
          </Button>
        </Box>
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Name</TableCell>
                <TableCell>ID</TableCell>
                <TableCell>Delete</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {objects.map((o) => (
                <TableRow>
                  <TableCell>{o.name}</TableCell>
                  <TableCell>{o.id}</TableCell>
                  <TableCell>
                    <Button
                      onClick={() => {
                        handleRemoveObject(o.id);
                      }}
                      variant="contained"
                      color="error"
                    >
                      Delete object
                    </Button>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </Stack>
      <EditUser
        user={userToEdit}
        modal={editUserModal}
        close={setEditUserModal}
        objects={objects}
      ></EditUser>
      <NewObject
        group={id}
        modal={newObjectModal}
        close={setNewObjectModal}
      ></NewObject>
      <NewUser
        group={id}
        modal={newUserModal}
        close={setNewUserModal}
      ></NewUser>
    </div>
  );
}
export default Group;
