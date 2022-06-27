import React from "react";
import Header from "./Header";
import AuthService from "../Services/AuthService";
import { useState, useEffect } from "react";
import axios from "axios";
import environment from "../Constants/Environment";
import moment from "moment";
import Moment from "react-moment";

export default function Logs() {
  const [logs, setLogs] = useState([]);

  axios.defaults.withCredentials = true;

  useEffect(() => {
    axios
      .get(environment.baseURL + "logs", {
        params: {
          type: "INFO",
          start: moment(new Date())
            .subtract({ hours: 60 })
            .format("DD/MM/YYYY HH:mm:ss"),
          user: "user1",
          desc: "all",
        },
      })
      .then((response) => {
        console.log(response.data);
        setLogs(response.data);
      });
  }, []);

  return (
    <div>
      <Header user={AuthService.getUser()}></Header>
      {logs.map((l) => (
        <div>
          {l.desc} at <Moment>{l.timestamp}</Moment>
        </div>
      ))}
    </div>
  );
}
