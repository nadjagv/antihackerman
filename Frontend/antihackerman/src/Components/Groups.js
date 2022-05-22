import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Button,
} from "@mui/material";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

const testGroups = [
  { name: "Hajduci", id: 1 },
  {
    name: "Sigme",
    id: 2,
  },
];

function Groups() {
  const [groups, setGroups] = useState(testGroups);
  console.log(groups);
  const navigation = useNavigate();
  return (
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
    </TableContainer>
  );
}
export default Groups;
