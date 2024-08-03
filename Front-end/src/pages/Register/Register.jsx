import React, { useState } from "react";
import { Form, Input } from "antd";
import login from "../../assets/login.jpg"
import { NavLink, useNavigate } from "react-router-dom";
import axios from "axios"; 
import Swal from "sweetalert2";

const formItemLayout = {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 },
};

export default function Register() {
  const navigate = useNavigate();

  const [userName, setUserName] = useState("");
  const [emailAddress, setEmailAddress] = useState("");
  const [password, setPassword] = useState("");
  const [telephone, setTelephone] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const handleRegister = () => {
    if (password !== confirmPassword) {
      Swal.fire({
        icon: "error",
        title: "Oops...",
        text: "Confirm password does not match!",
      });
      return;
    }

    const myHeaders = {
      "Content-Type": "application/json",
    };

    const data = {
      userName: userName,
      emailAddress: emailAddress,
      pass: password,
      telephone: telephone,
    };

    axios
      .post("http://localhost:8080/api/auth/register", data, { headers: myHeaders })
      .then((response) => {
        console.log(response);
        if (response.status === 200) {
          Swal.fire({
            position: "center",
            icon: "success",
            title: "Welcome",
            html: "<h3>Register Successfully</h3>",
            showConfirmButton: false,
            timer: 1600,
          }).then((result) => {
            if (result.dismiss === Swal.DismissReason.timer) {
              navigate("/");
            }
          });
        } else {
          throw new Error(response.statusText);
        }
      })
      .catch((error) => {
        Swal.fire({
          icon: "error",
          title: "Oops...",
          text: "Register is invalid!",
          footer: '<a href="/">Try again!</a>',
        });
      });
  };


  return (
    <div className="bg-[#f5f5f5] w-full h-screen flex items-start">
      <div className="w-1/2 h-full flex flex-col p-20 justify-between items-center">
        <div className="w-full flex flex-col max-w-[500px]">
          <div className="w-full flex flex-col mb-10 items-center">
            <h3 className="text-5xl font-bold text-[#060606] mb-4">
              Create New Account
            </h3>
            <p className="text-black/60 italic">
              Please fill in the form to continue.
            </p>
          </div>

          <div className="w-full">
            <Form
              {...formItemLayout}
              size="large"
              style={{
                maxWidth: 600,
              }}
              initialValues={{
                remember: true,
              }}
              autoComplete="off"
              onSubmit={handleRegister}
            >
              <Form.Item
                className="mx-0 px-0 w-full"
                name="userName"
                rules={[
                  {
                    required: true,
                    message: "Please input your user name!",
                  },
                  {
                    pattern: /^.{3,}$/,
                    message: "Username must be greater than 3 characters!",
                  },
                ]}
              >
                <Input
                  className="w-full px-4 py-2.5 rounded-lg border-[#d9d9d9]"
                  name="userName"
                  placeholder="Enter your user name"
                  value={userName}
                  onChange={(e) => setUserName(e.target.value)}
                />
              </Form.Item>

              <Form.Item
                className="mx-0 px-0 w-full"
                name="emailAddress"
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
                  className="w-full px-4 py-2.5 rounded-lg border-[#d9d9d9]"
                  name="emailAddress"
                  placeholder="Enter your email"
                  value={emailAddress}
                  onChange={(e) => setEmailAddress(e.target.value)}
                />
              </Form.Item>

              <Form.Item
                className="mx-0 px-0 w-full"
                name="password"
                rules={[
                  {
                    required: true,
                    message: "Please input your password!",
                  },
                  {
                    pattern: /^.{3,}$/,
                    message: "Password must be greater than 7 characters!",
                  },
                ]}
              >
                <Input.Password
                  className="w-full px-4 py-2.5"
                  name="password"
                  placeholder="Enter your password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
              </Form.Item>

              <Form.Item
                className="mx-0 px-0 w-full"
                name="confirmPassword"
                rules={[
                  {
                    required: true,
                    message: "Please input your confirm password!",
                  },
                  {
                    pattern: /^.{3,}$/,
                    message: "Password must be greater than 7 characters!",
                  },
                ]}
              >
                <Input.Password
                  className="w-full px-4 py-2.5"
                  placeholder="Enter your confirm password"
                  value={confirmPassword}
                  onChange={(e) => setConfirmPassword(e.target.value)}
                />
              </Form.Item>

              <Form.Item
                className="mx-0 px-0 w-full"
                name="telephone"
                rules={[
                  {
                    required: true,
                    message: "Please input your telephone!",
                  },
                  {
                    pattern: /^\d{10}$/,
                    message: "Telephone number must be exactly 10 digits!",
                  },
                ]}
              >
                <Input
                  className="w-full px-4 py-2.5 rounded-lg border-[#d9d9d9]"
                  placeholder="Enter your telephone number"
                  name="telephone"
                  value={telephone}
                  onChange={(e) => setTelephone(e.target.value)}
                />
              </Form.Item>


              <div className="w-full flex flex-col my-3">
                <button
                  className="w-full bg-[#060606] text-white my-2 font-semibold rounded-md p-4 text-center flex items-center justify-center focus:outline-none hover:bg-[#2f6a81] "
                  type="submit"
                  onClick={handleRegister}
                >
                  Sign Up
                </button>
              </div>
            </Form>
          </div>

          <div className="w-full flex items-center justify-center">
            <p className="text-sm font-normal text-black/70">
              Already have an account?
              <span className="font-semibold underline underline-offset-1 ml-1">
                <NavLink to={"/"}>
                  <button className="hover:outline-none hover:font-bold hover:text-[#2f6a81] underline underline-offset-2 bg-[#f5f5f5] text-red-700 ">
                    Sign In
                  </button>
                </NavLink>
              </span>
            </p>
          </div>
        </div>
      </div>

      <div className="relative w-1/2 h-full flex flex-col">
        <div className="absolute top-[20%] right-[10%] flex flex-col z-10 text-right">
          <h1 className="text-7xl text-white font-bold my-5">
            Artwork Sharing Platform
          </h1>
          <p className="text-xl text-white font-normal">
            Start for free and get attractive offers from the community
          </p>
        </div>
        <div className="relative mr-8 mt-8 mb-8 w-100 h-full">
          <img src={login} className="w-full h-full rounded-3xl" />
          <div className="absolute rounded-3xl top-0 left-0 w-full h-full bg-black opacity-50"></div>
        </div>
      </div>
    </div>
  );
}
