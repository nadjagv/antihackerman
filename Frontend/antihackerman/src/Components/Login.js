import { Grid, Paper, TextField, Button } from "@mui/material";
import { useState } from "react";

function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const paperStyle = {
    padding: 20,
    height: "35vh",
    width: 280,
    margin: "20px auto",
  };
  const btnstyle = { margin: "8px 0" };
  return (
    <Grid>
      <Paper elevation={10} style={paperStyle}>
        <Grid>
          <h2>Sign In</h2>
        </Grid>{" "}
        <TextField
          margin="normal"
          label="Username"
          placeholder="Enter username"
          fullWidth
          required
        ></TextField>
        <TextField
          margin="normal"
          label="Password"
          placeholder="Enter password"
          type="password"
          fullWidth
          required
        ></TextField>
        <Button
          type="submit"
          color="primary"
          variant="contained"
          style={btnstyle}
          fullWidth
        >
          Sign in
        </Button>
      </Paper>
    </Grid>
  );
}
export default Login;
