import {
  TableContainer,
  TableHead,
  Table,
  TableRow,
  TableCell,
  TableBody,
  Button,
} from "@mui/material";
import React from "react";
import { useState } from "react";

const testData = [
  {
    commonName: "Hajduk Karting",
    organizationUnit: "embedded",
    organization: "Not VegaIT",
    locality: "Sid",
    state: "Vojvodina",
    country: "Serbia",
  },
];

function Certificates() {
  const [certificates, setCertificates] = useState(testData);

  return (
    <div>
      <TableContainer>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Common Name</TableCell>
              <TableCell>Organisation Unit</TableCell>
              <TableCell>Organization</TableCell>
              <TableCell>Locality</TableCell>
              <TableCell>State</TableCell>
              <TableCell>Country</TableCell>
              <TableCell></TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {certificates.map((cert) => (
              <TableRow key={cert.commonName}>
                <TableCell>{cert.commonName}</TableCell>
                <TableCell>{cert.organizationUnit}</TableCell>
                <TableCell>{cert.organization}</TableCell>
                <TableCell>{cert.locality}</TableCell>
                <TableCell>{cert.state}</TableCell>
                <TableCell>{cert.country}</TableCell>
                <TableCell>
                  <Button variant="outlined" color="error">
                    Revoke
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
export default Certificates;
