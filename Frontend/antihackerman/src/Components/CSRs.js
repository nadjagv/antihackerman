import React, { useState, useEffect } from "react";
import environment from "../Constants/Environment";
import {
  TableContainer,
  TableHead,
  Table,
  TableRow,
  TableCell,
  TableBody,
  Button,
} from "@mui/material";
import axios from "axios";

function CSRs() {
  const [csrs, setCsrs] = useState([]);
  useEffect(() => {
    axios.get(environment.baseURL + "csr").then((response) => {
      setCsrs(response.data);
      //console.log(response.data);
    });
  }, []);

  const rejectCSR = (filename) => {
    axios
      .get(environment.baseURL + "csr/reject/" + filename)
      .then((reponse) => {
        console.log("Success");
      });
  };
  return (
    <div>
      <TableContainer>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Common Name</TableCell>
              <TableCell>Country</TableCell>
              <TableCell>Email</TableCell>
              <TableCell>Given name</TableCell>
              <TableCell>Organisation Unit</TableCell>
              <TableCell>Organization</TableCell>
              <TableCell>Surname</TableCell>
              <TableCell>Uid</TableCell>

              <TableCell></TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {csrs.map((csr) => (
              <TableRow key={csr.commonName}>
                <TableCell>{csr.commonName}</TableCell>
                <TableCell>{csr.country}</TableCell>
                <TableCell>{csr.email}</TableCell>
                <TableCell>{csr.givenname}</TableCell>
                <TableCell>{csr.organizationUnit}</TableCell>
                <TableCell>{csr.organization}</TableCell>
                <TableCell>{csr.surname}</TableCell>
                <TableCell>{csr.uid}</TableCell>

                <TableCell>
                  <Button
                    onClick={(e) => {
                      rejectCSR(csr.uniqueFilename);
                    }}
                    variant="outlined"
                    color="error"
                  >
                    Deny request
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
export default CSRs;
