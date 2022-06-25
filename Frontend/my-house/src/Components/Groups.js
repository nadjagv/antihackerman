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
import AuthService from "../Services/AuthService";
import Header from "./Header";

const testGroups = [
  { name: "Hajduci", id: 1 },
  {
    name: "Sigme",
    id: 2,
  },
];

function Groups() {
  const [groups, setGroups] = useState([]);
  const navigation = useNavigate();

  axios.defaults.withCredentials = true;

  useEffect(() => {
    axios
      .get(environment.baseURL + "groups/" + AuthService.getUser().username)
      .then((response) => {
        setGroups(response.data);
      });
  }, []);

  return (
    <div>
      <Header user={AuthService.getUser()}></Header>
      <TableContainer>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Group name</TableCell>
              <TableCell>Group ID</TableCell>
              <TableCell>View group</TableCell>
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
                    View
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
export default Groups;
