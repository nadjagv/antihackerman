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

const roles = ["Owner", "Tenant"];

function AddUser(props) {
  const [role, setRole] = useState("");
  const [userObjects, setUserObjects] = useState([]);
  const [options, setOptions] = useState([]);
  const [users, setUsers] = useState([]);
  const [user, setUser] = useState();

  axios.defaults.withCredentials = true;

  useEffect(() => {
    axios
      .get(environment.baseURL + "groups/outsiders/" + props.group)
      .then((response) => {
          console.log(response.data)
        setUsers(response.data);
      });
    if (props.user.roleForGroup === "Tenant") {
      axios
        .get(
          environment.baseURL + "users/realestatesTenanting/" + props.user.id
        )
        .then((response) => {
          const objects = [...response.data];
          const objects2 = objects.filter((obj) => obj.groupId == props.group);
          setUserObjects(objects2);
        });
    }
    setOptions(props.objects);
    setRole(props.user.roleForGroup);
  }, [props]);
  const addUser = () => {
    if (role === "Tenant") {
      let realestate_ids = userObjects.map((object) => object.id);
      if (realestate_ids.length === 0) {
        alert("Need to select at least 1 object!");
      } else {
        axios.put(
          environment.baseURL +
            "users/to-tenant/" +
            props.user.id +
            "/" +
            props.group,
          realestate_ids
        );
        props.close(false);
      }
    } else {
      let realestate_ids = options.map((object) => object.id);
      axios.put(
        environment.baseURL +
          "users/to-owner/" +
          props.user.id +
          "/" +
          props.group,
        realestate_ids
      );
      props.close(false);
    }
  };
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
          Add existing user
        </Typography>

        <Stack spacing="3" alignItems="center">
          <Autocomplete
            value={user}
            options={users.map((u) => u)}
            disableClearable={true}
            sx={{ width: 225 }}
            getOptionLabel={(option) => option.username}
            renderOption={(props, option, { selected }) => (
                <li {...props}>
                  <Checkbox style={{ marginRight: 8 }} checked={selected} />
                  {option.username}
                </li>
              )}
            renderInput={(params) => <TextField {...params} label="User" />}
            onChange={(event, newValue) => {
              setUser(newValue);
            }}
          ></Autocomplete>
          { user && <Autocomplete
            value={role}
            options={roles}
            disableClearable={true}
            sx={{ width: 225 }}
            renderInput={(params) => <TextField {...params} label="Role" />}
            onChange={(event, newValue) => {
              setRole(newValue);
            }}
          ></Autocomplete>}
          {role === "Tenant" && (
            <Autocomplete
              multiple
              id="checkboxes-tags-objects"
              value={userObjects}
              options={options.map((o) => o)}
              isOptionEqualToValue={(option, value) => option.id === value.id}
              disableCloseOnSelect
              disableClearable={true}
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
          )}
          <Button
            onClick={() => {
              addUser();
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
export default AddUser;
