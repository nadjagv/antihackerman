import {
  Modal,
  Box,
  Typography,
  TextField,
  Stack,
  Button,
} from "@mui/material";
import React from "react";
import { useState, useEffect } from "react";
import axios from "axios";
import environment from "../Constants/Environment";
import modalStyle from "../Constants/Styles";

function NewObject(props) {
  let open = props.modal;
  if (open === undefined) {
    open = false;
  }
  const [name, setName] = useState("");
  const createObject = () => {};
  const closeModal = () => {
    props.close(false);
  };
  return (
    <Modal
      open={open}
      onClose={() => {
        closeModal();
      }}
      aria-labelledby="modal-modal-title"
      aria-describedby="modal-modal-description"
    >
      <Box sx={modalStyle}>
        <Typography id="modal-modal-title" variant="h6" component="h2">
          Add object
        </Typography>
        <Stack spacing="3" alignItems="center">
          <TextField
            id="outlined-name-input"
            label="Name"
            type="text"
            onChange={(e) => setName(e.target.value)}
            value={name}
          />
          <Button
            onClick={() => {
              createObject();
            }}
            variant="contained"
            color="success"
          >
            Create object
          </Button>
        </Stack>
      </Box>
    </Modal>
  );
}
export default NewObject;
