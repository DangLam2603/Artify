// import axios from "axios";
// import { loginFailed, loginStart, loginSuccess } from "./authSlice";

// export const loginUser = async(user, dispatch, navigate) => {
//     dispatch(loginStart());
//     try {
//         const res = await axios.post("http://localhost:8080/api/auth/login", user);
//         dispatch(loginSuccess(res.data));
//         navigate("/home");
//     }
//     catch (err) {
//         dispatch(loginFailed());
//     }
// }
