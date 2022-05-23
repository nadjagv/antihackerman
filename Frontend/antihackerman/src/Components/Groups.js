import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Button,
  Box,
} from "@mui/material";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import environment from "../Constants/Environment";
import axios from "axios";
import NewGroup from "./NewGroup";

const testGroups = [
  { name: "Hajduci", id: 1 },
  {
    name: "Sigme",
    id: 2,
  },
];

function Groups() {
  const [groups, setGroups] = useState([]);
  const [newGroupModal, setNewGroupModal] = useState(false);
  const navigation = useNavigate();

  axios.defaults.withCredentials = true;

  useEffect(() => {
    axios.get(environment.baseURL + "groups").then((response) => {
      setGroups(response.data);
    });
  }, []);

  const handleNewGroupModal = () => {
    setNewGroupModal(true);
  };

  return (
    <div>
      <TableContainer>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Group name</TableCell>
              <TableCell>Group ID</TableCell>
              <TableCell>View</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {groups.map((g) => (
              <TableRow>
                <TableCell>{g.name}</TableCell>
                <TableCell>{g.id}</TableCell>
                <TableCell>
                  <Button
                    onClick={() => {
                      navigation("/group/" + g.id);
                    }}
                    variant="contained"
                    color="success"
                  >
                    Manage Group
                  </Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
        <Box marginTop={5} marginBottom={5}>
          <Button onClick={() => {
            handleNewGroupModal();
          }} variant="contained" color="success">
            Add Group
          </Button>
        </Box>
      </TableContainer>
      <NewGroup
        modal={newGroupModal}
        close={setNewGroupModal}
      ></NewGroup>
    </div>
  );
}
export default Groups;
