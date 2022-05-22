const getUser = () => {
  const user = JSON.parse(sessionStorage.getItem("user"));
  return user;
};

const setUser = (user) => {
  sessionStorage.setItem("user", JSON.stringify(user));
};

const removeUser = () => {
  sessionStorage.removeItem("user");
};
export default { getUser, setUser, removeUser };
