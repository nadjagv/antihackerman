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
  Modal,
  Box,
  Typography,
  Autocomplete,
  Checkbox,
  TextField,
} from "@mui/material";
import axios from "axios";
import modalStyle from "../Constants/Styles";
import { type } from "@testing-library/user-event/dist/type";
import Header from "./Header";
import AuthService from "../Services/AuthService";

const extensions = [
  "AUTHORITY_KEY_IDENTIFIER",
  "BASIC_CONSTRAINTS",
  "KEY_USAGE",
  "SUBJECT_KEY_IDENTIFIER",
  "EXTENDED_KEY_USAGE",
  "SUBJECT_ALTERNATIVE_NAME",
];
function CSRs() {
  const [csrs, setCsrs] = useState([]);
  const [signModal, setSignModal] = useState(false);
  const [csrToSign, setCsrToSign] = useState("");
  const [selectedExtendsions, setSelectedExtendsions] = useState([]);

  axios.defaults.withCredentials = true

  useEffect(() => {
    axios.get(environment.baseURL + "csr").then((response) => {
      setCsrs(response.data);
    });
  }, []);

  const rejectCSR = (filename) => {
    axios
      .get(environment.baseURL + "csr/rejectCSR/" + filename)
      .then((response) => {
        console.log("Success");
      });
  };
  const acceptCSR = () => {
    let extensions = [];
    selectedExtendsions.forEach((element) =>
      extensions.push({ type: element, citical: false })
    );
    axios
      .post(
        environment.baseURL + "csr/approveCSRextensions/" + csrToSign,
        extensions
      )
      .then((response) => {
        console.log("Success");
        setSignModal(false);
      });
  };
  return (
    <div>
      <Header user={AuthService.getUser()}></Header>
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
                  <Button
                    onClick={(e) => {
                      setCsrToSign(csr.uniqueFilename);
                      setSignModal(true);
                    }}
                    variant="outlined"
                    color="success"
                  >
                    Accept request
                  </Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
      <Modal
        open={signModal}
        onClose={() => {
          setSignModal(false);
        }}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description"
      >
        <Box sx={modalStyle}>
          <Typography id="modal-modal-title" variant="h6" component="h2">
            Certificate details
          </Typography>
          <div
            id="modal-modal-description"
            sx={{ mt: 2 }}
            className="modal-text"
          >
            <Autocomplete
              multiple
              id="checkboxes-tags-demo"
              value={selectedExtendsions}
              options={extensions}
              disableCloseOnSelect
              onChange={(e, newValue) => {
                setSelectedExtendsions(newValue);
              }}
              getOptionLabel={(option) => option}
              renderOption={(props, option, { selected }) => (
                <li {...props}>
                  <Checkbox style={{ marginRight: 8 }} checked={selected} />
                  {option}
                </li>
              )}
              style={{ width: 500 }}
              renderInput={(params) => (
                <TextField
                  {...params}
                  label="Checkboxes"
                  placeholder="Extensions"
                />
              )}
            />
            <Button onClick={acceptCSR} variant="contained" color="success">
              Approve CSR
            </Button>
          </div>
        </Box>
      </Modal>
    </div>
  );
}
export default CSRs;
