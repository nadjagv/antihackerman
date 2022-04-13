import { TextField, Box, Stack, Typography, Button } from "@mui/material";
import React from "react";

function NewCSR() {
  return (
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
          />
          <TextField
            id="outlined-organisation-unit-input"
            label="Organisation Unit"
            type="text"
          />
          <TextField
            id="outlined-organisation-input"
            label="Organisation"
            type="text"
          />
          <TextField
            id="outlined-locality-input"
            label="Locality"
            type="text"
          />
          <TextField id="outlined-state-input" label="State" type="text" />
          <TextField id="outlined-state-input" label="State" type="text" />
          <TextField id="outlined-country-input" label="Country" type="text" />
          <Button variant="contained" color="success">
            Submit request
          </Button>
        </Stack>
      </Box>
    </Box>
  );
}
export default NewCSR;
