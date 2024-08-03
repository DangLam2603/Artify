import React, { useState } from "react";
import { Checkbox, Form, Input } from "antd";
import "./Login.scss";
import { NavLink, useNavigate } from "react-router-dom";
import GoogleButton from "react-google-button";
import login from "../../assets/login.jpg"
import axios from "axios";
import Swal from "sweetalert2";

const formItemCol = {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 },
};

export default function Login() {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = () => {
    const myHeaders = {
      "Content-Type": "application/json",
    };

    const data = {
      email: email,
      pass: password,
    };

    axios
      .post("http://localhost:8080/api/auth/login", data, { headers: myHeaders })
      .then((response) => {
        console.log(response.data.payload);
        if (response.status === 200) {
          const token = response.data.payload.token;
          localStorage.setItem("token", token);
          setCookie("token", token, 365); // Expires in 365 days
          Swal.fire({
            position: "center",
            icon: "success",
            title: `Welcome`,
            html: "<h3>Login Successfully</h3>",
            showConfirmButton: false,
            timer: 1600
          }).then(() => {
            navigate("/home");
          });
        } else {
          throw new Error(response.statusText);
        }
      })
      .catch((error) => {
        Swal.fire({
          icon: "error",
          title: "Oops...",
          text: "Email or password is invalid!",
          footer: '<a href="/">Try again!</a>'
        });
      });
  };

  const setCookie = (name, value, days) => {
    const date = new Date();
    date.setTime(date.getTime() + days * 24 * 60 * 60 * 1000);
    const expires = "; expires=" + date.toGMTString();
    document.cookie = name + "=" + value + expires + "; path=/";
  };


  return (
    <div className="bg-gray-100 w-full h-screen flex items-center">
      <div className="relative w-1/2 h-full flex flex-col">
        <div className="absolute top-[20%] left-[10%] flex flex-col z-10">
          <h1 className="text-7xl text-white font-bold my-5">
            Artwork Sharing Platform
          </h1>
          <p className="text-xl text-white font-normal">
            Start for free and get attractive offers from the community
          </p>
        </div>
        <div className="relative ml-8 mt-8 mb-8 w-100 h-full">
          <img src={login} className="w-full h-full rounded-3xl" style={{ width: "1068px" }} />
          <div className="absolute rounded-3xl top-0 left-0 w-full h-full bg-black opacity-50"></div>
        </div>
      </div>

      <div className="w-1/2 h-full flex flex-col p-20 justify-center items-center">
        <div className="w-full flex flex-col max-w-[500px]">
          <div className="w-full flex flex-col mb-10 items-center">
            <h3 className="text-5xl text-[#060606] font-bold mb-4">
              Login
            </h3>
            <p className="text-black/60 mb-2 italic">
              Welcome Back! Please enter your details.
            </p>
          </div>

          <div className="w-full">
            <Form
              {...formItemCol}
              size="large"
              style={{
                maxWidth: 600,
              }}
              initialValues={{
                remember: true,
              }}
              autoComplete="off"
              onSubmit={handleLogin}
            >
              <Form.Item
                className="mx-0 px-0 w-full pt-5"
                name="email"
                rules={[
                  {
                    required: true,
                    message: "Please input your email!",
                  },
                  {
                    pattern:
                      /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
                    message: "Please input valid email!",
                  },
                ]}
              >
                <Input
                  className="w-full px-5 py-2.5 rounded-lg border-[#d9d9d9]"
                  name="email"
                  placeholder="Enter your email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                />
              </Form.Item>

              <Form.Item
                className="mx-0 px-0 w-full pt-5"
                name="password"
              >
                <Input.Password
                  className="w-full px-5 py-2.5"
                  placeholder="Enter your password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  name="password"
                />
              </Form.Item>

              {/* <Form.Item
                name="remember"
                valuePropName="checked"
                {...formItemCol}
              >
                <div
                  className="flex items-center"
                  style={{ justifyContent: "space-between", width: "100%" }}
                >
                  <Checkbox>Remember me</Checkbox>
                  <p className="font-bold cursor-pointer hover:text-[#2f6a81]">Forgot Password</p>
                </div>
              </Form.Item> */}

              <div className="w-full flex flex-col my-5">
                <button
                  className="w-full bg-[#2f6a81] border-2 text-white text- my-2 font-semibold rounded-md p-3 text-center flex items-center justify-center focus:outline-none hover:bg-gray-100 hover:text-[#2f6a81] hover:border-[#2f6a81] hover:border-2 mt-3 transition-all duration-300 "
                  type="submit"
                  onClick={handleLogin}
                >
                  Sign In
                </button>
              </div>
            </Form>

            {/* <div className="w-full py-2 flex items-center justify-center relative">
              <div className="w-full h-[1px] bg-black/25"></div>
              <p className="absolute  p-2 text-black/60 bg-[#f5f5f5]">or</p>
            </div>

            <div className="w-full flex items-center justify-center my-4">
              <GoogleButton
                type="light"
                style={{
                  width: "100%",
                  borderRadius: "0.375rem",
                  fontSize: ".9em"
                }}
                className="my-2"
                onClick={() => alert("Google button clicked")}
              />
            </div> */}
          </div>

          <div className="w-full flex items-center justify-center">
            <p className="text-sm font-normal text-black/70">
              Don't have an account?
              <span className="font-semibold underline underline-offset-1 ml-1">
                <NavLink to={"/register"}>
                  <button className="hover:outline-none hover:text-[#2f6a81] underline underline-offset-2 bg-[#f5f5f5] text-red-700 hover:font-bold">Sign Up</button>
                </NavLink>
              </span>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}
