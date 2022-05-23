import { TextField, Box, Stack, Typography, Button } from "@mui/material";
import React from "react";
import { useState } from "react";
import environment from "../Constants/Environment";
import axios from "axios";
import Header from "./Header";
import AuthService from "../Services/AuthService";

function NewCSR() {
  const [commonName, setCommonName] = useState("");
  const [surname, setSurname] = useState("");
  const [givenname, setGivanname] = useState("");
  const [organizationUnit, setOrganizationUnit] = useState("");
  const [organization, setOrganization] = useState("");
  const [country, setCountry] = useState("");
  const [email, setEmail] = useState("");
  const [uid, setUid] = useState("");

  axios.defaults.withCredentials = true;

  const newCSR = () => {
    let CSR = {
      commonName: commonName,
      surname: surname,
      givenname: givenname,
      organizationUnit: organizationUnit,
      organization: organization,
      country: country,
      email: email,
      uid: uid,
    };
    axios
      .post(environment.baseURL + "csr/generateCSR", CSR)
      .then((response) => {
        console.log("success");
      });
  };
  return (
    <div>
      <Header user={AuthService.getUser()}></Header>
      <Box>
        <Box
          component="form"
          sx={{
            "& .MuiTextField-root": { m: 1, width: "25ch" },
          }}
          noValidate
          autoComplete="off"
        >
          <Typography variant="h6">Create new CSR</Typography>
          <Stack spacing="3" alignItems="center">
            <TextField
              id="outlined-common-name-input"
              label="Common Name"
              type="text"
              onChange={(e) => setCommonName(e.target.value)}
              value={commonName}
            />
            <TextField
              id="outlined-surname-input"
              label="Surname"
              type="text"
              onChange={(e) => setSurname(e.target.value)}
              value={surname}
            />
            <TextField
              id="outlined-givenname-input"
              label="Given name"
              type="text"
              onChange={(e) => setGivanname(e.target.value)}
              value={givenname}
            />
            <TextField
              id="outlined-organization-unit-input"
              label="Organisation Unit"
              type="text"
              onChange={(e) => setOrganizationUnit(e.target.value)}
              value={organizationUnit}
            />
            <TextField
              id="outlined-organisation-input"
              label="Organization"
              type="text"
              onChange={(e) => setOrganization(e.target.value)}
              value={organization}
            />
            <TextField
              id="outlined-country-input"
              label="Country"
              type="text"
              onChange={(e) => setCountry(e.target.value)}
              value={country}
            />
            <TextField
              id="outlined-email-input"
              label="Email"
              type="text"
              onChange={(e) => setEmail(e.target.value)}
              value={email}
            />
            <TextField
              id="outlined-uid-input"
              label="Uid"
              type="text"
              onChange={(e) => setUid(e.target.value)}
              value={uid}
            />

            <Button
              onClick={() => {
                newCSR();
              }}
              variant="contained"
              color="success"
            >
              Submit request
            </Button>
          </Stack>
        </Box>
      </Box>
    </div>
  );
}
export default NewCSR;
