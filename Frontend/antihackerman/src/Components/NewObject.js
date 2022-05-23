import {
  Modal,
  Box,
  Typography,
  TextField,
  Stack,
  Button,
} from "@mui/material";
import React from "react";
import { useState } from "react";
import axios from "axios";
import environment from "../Constants/Environment";
import modalStyle from "../Constants/Styles";

function NewObject(props) {
  let open = props.modal;
  if (open === undefined) {
    open = false;
  }
  const [name, setName] = useState("");
  const [location, setLocation] = useState("");

  axios.defaults.withCredentials = true

  const createObject = () => {
    if(name!=="" && location!==""){
      axios.post(environment.baseURL +'real-estates',{name: name, location: location,groupId: props.group})
      props.close(false);
      window.location.reload();
    }
    else{
      alert("Empty fields!")
    }
  };
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
          <TextField
            id="outlined-name-input"
            label="Location"
            type="text"
            onChange={(e) => setLocation(e.target.value)}
            value={location}
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
