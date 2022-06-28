import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';
import environment from "../Constants/Environment";
import { NotificationManager} from 'react-notifications';

let stompClient;

const getUser = () => {
  const user = JSON.parse(sessionStorage.getItem("user"));
  return user;
};

const isAuth = ()=>{
  const user = JSON.parse(sessionStorage.getItem("user"));
  return user===null;
}

const setUser = (user) => {
  sessionStorage.setItem("user", JSON.stringify(user));

  const socket = new SockJS(environment.baseURL + 'websocket');
  stompClient = Stomp.over(socket);


  stompClient.connect({}, (frame) => {
    stompClient.subscribe('/topic/simple-notification',(message) => {
      console.log(JSON.parse(message.body).content)
      NotificationManager.info(JSON.parse(message.body).content);
    });
  });
};

const removeUser = () => {
  stompClient.disconnect()
  sessionStorage.removeItem("user");
};
export default { getUser, setUser, removeUser,isAuth };
