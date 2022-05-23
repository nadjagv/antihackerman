import {
  Modal,
  Box,
  Typography,
  TextField,
  Stack,
  Autocomplete,
  Checkbox,
  Button,
} from "@mui/material";
import React from "react";
import { useState, useEffect } from "react";
import axios from "axios";
import environment from "../Constants/Environment";
import modalStyle from "../Constants/Styles";

const roles = ["Admin", "Owner", "Tenant"];

function EditUser(props) {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [role, setRole] = useState("");
  const [userObjects, setUserObjects] = useState([]);
  const [options, setOptions] = useState([]);
  useEffect(() => {
    setUsername(props.user.username);
    setEmail(props.user.email);
    setOptions(props.objects);
    if (props.user.roles) {
      if (props.user.roles.includes("ROLE_ADMIN")) {
        setRole("Admin");
      } else {
        if (props.user.roles.includes("ROLE_OWNER")) {
          setRole("Owner");
        } else {
          if (props.user.roles.includes("ROLE_TENANT")) {
            setRole("Tenant");
          }
        }
      }
    }
  }, [props]);
  const changeUser = () => {};
  const closeModal = () => {
    props.close(false);
  };
  return (
    <Modal
      open={props.modal}
      onClose={() => {
        closeModal();
      }}
      aria-labelledby="modal-modal-title"
      aria-describedby="modal-modal-description"
    >
      <Box sx={modalStyle}>
        <Typography id="modal-modal-title" variant="h6" component="h2">
          Edit user
        </Typography>
        <Stack spacing="3" alignItems="center">
          <TextField
            id="outlined-username-input"
            disabled={true}
            label="Username"
            type="text"
            onChange={(e) => setUsername(e.target.value)}
            value={username}
          />
          <TextField
            id="outlined-email-input"
            label="Email"
            type="text"
            onChange={(e) => setEmail(e.target.value)}
            value={email}
          />
          <Autocomplete
            value={role}
            options={roles}
            sx={{ width: 225 }}
            renderInput={(params) => <TextField {...params} label="Role" />}
            onChange={(event, newValue) => {
              setRole(newValue);
            }}
          ></Autocomplete>
          <Autocomplete
            multiple
            id="checkboxes-tags-objects"
            value={userObjects}
            options={options.map((o) => o)}
            disableCloseOnSelect
            onChange={(e, newValue) => {
              setUserObjects(newValue);
            }}
            getOptionLabel={(option) => option.name}
            renderOption={(props, option, { selected }) => (
              <li {...props}>
                <Checkbox style={{ marginRight: 8 }} checked={selected} />
                {option.name}
              </li>
            )}
            style={{ width: 500 }}
            renderInput={(params) => (
              <TextField {...params} label="Objects" placeholder="Objects" />
            )}
          />
          <Button
            onClick={() => {
              changeUser();
            }}
            variant="contained"
            color="success"
          >
            Save changes
          </Button>
        </Stack>
      </Box>
    </Modal>
  );
}
export default EditUser;
