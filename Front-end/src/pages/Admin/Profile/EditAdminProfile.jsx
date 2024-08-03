import {
    CloseOutlined,
} from '@ant-design/icons';
import { Avatar, Button, Form, Input, Spin } from "antd";
import axios from "axios";
import { Card, Modal } from "flowbite-react";
import React, { useEffect, useState } from "react";
import { MdOutlineModeEdit } from "react-icons/md";
import { useNavigate } from "react-router-dom";
import Swal from "sweetalert2";

const formItemCol = {
    labelCol: { span: 24 },
    wrapperCol: { span: 24 },
};

const EditAdminProfile = () => {
    const token = localStorage.getItem("token");
    const navigate = useNavigate();
    const [isLoading, setIsLoading] = useState(false);
    const [customer, setCustomer] = useState({});
    const [emailAddress, setEmailAddress] = useState("");
    const [userName, setUserName] = useState("");
    const [telephone, setTelephone] = useState("");
    const [password, setPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [confirmNewPassword, setConfirmNewPassword] = useState("");
    const [imagePath, setImagePath] = useState("");
    const [files, setFiles] = useState();
    const [openModal, setOpenModal] = useState(false);

    console.log(files);
    useEffect(() => {
        const fetchData = async () => {
            try {
                const customerResponse = await axios.get(
                    "http://localhost:8080/api/auth/user/profile",
                    {
                        headers: { Authorization: `Bearer ${token}` },
                    }
                );
                setCustomer(customerResponse.data.payload);
                setUserName(customerResponse.data.payload.userName);
                setTelephone(customerResponse.data.payload.telephone);
                setImagePath(customerResponse.data.payload.imagePath);
                if (customerResponse) setIsLoading(true);
            } catch (error) {
                console.error("Error fetching data:", error);
            }
        };
        fetchData();
    }, []);

    const handleEditProfile = () => {
        setIsLoading(false);
        const myHeaders = {
            "Content-Type": "multipart/form-data",
        };
        const updatedProfileDTO = {
            userName: userName,
            telephone: telephone,
        };
        const formData = new FormData();
        if (files != null && files.length > 0) {
            formData.append("image", files[0]);
        } else {
            formData.append("imagePath", imagePath);
        }
        formData.append(
            "user",
            new Blob([JSON.stringify(updatedProfileDTO)], { type: "application/json" })
        );
        console.log(formData);
        axios
            .put("http://localhost:8080/api/auth/user/profile", formData, {
                headers: { Authorization: `Bearer ${token}`, myHeaders },
            })
            .then((response) => {
                if (response) setIsLoading(true);
                if (response.status === 200) {
                    Swal.fire({
                        position: "center",
                        icon: "success",
                        title: `Done`,
                        html: "<h3>Update Successfully</h3>",
                        showConfirmButton: false,
                        timer: 1600,
                    });
                    navigate(`/viewEwallet`);
                } else {
                    throw new Error(response.statusText);
                }
            })
            .catch((error) => {
                Swal.fire({
                    icon: "error",
                    title: "Oops...",
                    text: "Something went wrong!",
                    footer: '<a href="/">Try again!</a>',
                });
            });
    };

    const handleAvatarChange = (event) => {
        const selectedFile = event.target.files[0];
        if (selectedFile) {
            setFiles([selectedFile]);
        }
    };


    const handleModalClose = () => {
        setOpenModal(true);
    };

    const handleChangePassword = async () => {
        if (!password || !newPassword || !confirmNewPassword) {
            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: "Please fill in all password fields!",
            });
            return;
        } else if (newPassword !== confirmNewPassword) {
            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: "Confirm password does not match!",
            });
            return;
        } else {
            const oldPassword = { password: password };
            const response = await axios.post(
                `http://localhost:8080/api/auth/user/profile/password`,
                oldPassword,
                {
                    headers: { Authorization: `Bearer ${token}` },
                }
            );
            if (response.status === 200) {
                if (response.data.payload === false) {
                    Swal.fire({
                        icon: "error",
                        title: "Oops...",
                        text: "Your password does not match the current one!",
                    });
                    return;
                } else {
                    const updatedPassword = { password: newPassword };
                    await axios
                        .put("http://localhost:8080/api/auth/user/profile/password", updatedPassword, {
                            headers: { Authorization: `Bearer ${token}` },
                        })
                        .then((response) => {
                            if (response) setIsLoading(true);
                            if (response.status === 200) {
                                Swal.fire({
                                    position: "center",
                                    icon: "success",
                                    title: `Done`,
                                    html: "<h3>Update Password Successfully</h3>",
                                    showConfirmButton: false,
                                    timer: 1600,
                                });
                                setOpenModal(false);
                            } else {
                                throw new Error(response.statusText);
                            }
                        })
                        .catch((error) => {
                            Swal.fire({
                                icon: "error",
                                title: "Oops...",
                                text: "Something went wrong!",
                                footer: '<a href="/">Try again!</a>',
                            });
                        });
                }
            }
        }
    }



    return (
        <div className="w-full h-full bg-gray-100 ">
            <Spin spinning={!isLoading} fullscreen />
            <Modal
                show={openModal}
                onClose={() => setOpenModal(false)}
                title="Change Password"
                className="mt-10 p-72 "
                size={1}
            >
                <h1 className="text-center font-semibold text-2xl mt-14">Change Password</h1>
                <p className="text-center mt-3">To change your password, please fill in the fields below.</p>
                <div className="pt-14 px-52" style={{ overflow: 'hidden' }}>
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
                    >
                        <Form.Item
                            className="mx-0 px-0 w-full pt-2.5"
                            name="password"
                        >
                            <Input.Password
                                className="w-full px-4 py-2.5"
                                placeholder="Enter your password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                name="password"
                            />
                        </Form.Item>
                        <Form.Item
                            className="mx-0 px-0 w-full pt-2.5"
                            name="newPassword"
                        >
                            <Input.Password
                                className="w-full px-4 py-2.5"
                                placeholder="Enter your new password"
                                value={newPassword}
                                onChange={(e) => setNewPassword(e.target.value)}
                                name="newPassword"
                            />
                        </Form.Item>
                        <Form.Item
                            className="mx-0 px-0 w-full pt-2.5"
                            name="confirmNewPassword"
                        >
                            <Input.Password
                                className="w-full px-4 py-2.5"
                                placeholder="Enter your confirm new password"
                                value={confirmNewPassword}
                                onChange={(e) => setConfirmNewPassword(e.target.value)}
                                name="confirmNewPassword"
                            />
                        </Form.Item>
                        <div className="w-full flex justify-center gap-4 my-4">
                            <Button
                                className="rounded-full border-2 w-[140px] bg-[#2f6a81] text-white my-2  px-4 py-2 text-center flex items-center justify-center focus:outline-none hover:bg-gray-100 hover:text-[#2f6a81] hover:border-[#2f6a81] hover:border-2 mt-3 transition-all duration-300 "
                                type="submit"
                                onClick={handleChangePassword}
                            >
                                Save Changes
                            </Button>
                            <Button
                                className="rounded-full border-2 w-[140px] border-[#2f6a81] bg-gray-100 text-[#2f6a81] my-2  px-4  py-2 text-center flex items-center justify-center focus:outline-none hover:bg-[#2f6a81] hover:text-white hover:border-white hover:border-2 mt-3 transition-all duration-300"
                                htmlType="reset"
                            >
                                Reset
                            </Button>
                        </div>
                    </Form>
                </div>
                <Button
                    className="absolute top-3 right-3"
                    type="text"
                    onClick={() => setOpenModal(false)} // Close the modal when clicking the "x" button
                    icon={<CloseOutlined />} // Display the "x" icon
                />
            </Modal>


            {isLoading && (
                <div className="h-screen w-full flex justify-center p-28 mt-10 gap-10">

                    <div className="w-1/2 flex flex-col">
                        <div className="relative h-[40%] bg-[#2f6a81]">
                            <div className="absolute flex flex-col items-center justify-center w-full top-1/3">
                                <Card className="w-[50%] h-[300px]">
                                    <div className="flex flex-col items-center">
                                        <div className="relative rounded-full shadow-2xl">
                                            <Avatar
                                                size="large"
                                                style={{ width: '225px', height: '225px' }}
                                                src={
                                                    files != null
                                                        ? URL.createObjectURL(files[0])
                                                        : customer.imagePath
                                                }
                                            />
                                            <label
                                                htmlFor="upload"
                                                className="absolute right-0 bottom-1 bg-[#2f6a81] rounded-full p-1.5 cursor-pointer"
                                            >
                                                <MdOutlineModeEdit size={20} color="white" />
                                            </label>
                                            <input
                                                type="file"
                                                id="upload"
                                                style={{ display: "none" }}
                                                onChange={handleAvatarChange}
                                            />
                                        </div>
                                        <h5 className="mt-3 text-xl font-medium text-gray-900 dark:text-white capitalize">
                                            {userName}
                                        </h5>
                                    </div>
                                </Card>
                            </div>
                        </div>

                        <div className="cursor-pointer w-[180px] flex justify-center gap-2 mx-auto text-white bg-[#2f6a81] px-4 py-2 transition-all duration-300 rounded-full my-1 mt-[30%]">
                            <button type="submit" onClick={handleModalClose}>Change Password</button>
                        </div>
                    </div>



                    <div className="w-1/2 h-full flex flex-col">
                        <h1 className="text-center text-3xl font-semibold mb-3 text-[#2f6a81]">
                            Edit Profile
                        </h1>
                        <p className="text-center mb-10 text-[#2f6a81]">
                            Update your profile information
                        </p>
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
                                onSubmit={handleEditProfile}
                                className="mx-auto"
                            >
                                <Form.Item
                                    className="m-2 px-0 w-full"
                                    name="emailAddress"
                                    label="Email"
                                >
                                    <Input
                                        className="w-full px-4 rounded-lg"
                                        name="emailAddress"
                                        placeholder={customer.emailAddress}
                                        value={customer.emailAddress}
                                        onChange={(e) => setEmailAddress(e.target.value)}
                                        disabled
                                    />
                                </Form.Item>
                                <Form.Item
                                    className="m-2 px-0 w-full"
                                    name="userName"
                                    label="Username"
                                    rules={[
                                        {
                                            required: true,
                                            message: "Please input your username!",
                                        },
                                        {
                                            pattern: /^[A-Za-z\s]{4,}$/,
                                            message:
                                                "Please input a valid username with more than 3 letters!",
                                        },
                                    ]}
                                >
                                    <Input
                                        className="w-full px-4 rounded-lg"
                                        name="username"
                                        placeholder={userName}
                                        onChange={(e) => setUserName(e.target.value)}
                                    />
                                </Form.Item>
                                <Form.Item
                                    className="m-2 px-0 w-full"
                                    name="telephone"
                                    label="Telephone"
                                    rules={[
                                        {
                                            pattern: /^0\d{9}$/,
                                            message:
                                                "Please input a valid phone number that starts with 0 and contains 10 digits!",
                                        },
                                    ]}
                                >
                                    <Input
                                        className="w-full px-4 rounded-lg"
                                        name="telephone"
                                        placeholder={telephone}
                                        onChange={(e) => setTelephone(e.target.value)}
                                    />
                                </Form.Item>
                                <div className="w-full flex justify-center gap-4 my-4">
                                    <Button
                                        className="rounded-full border-2 w-[140px] bg-[#2f6a81] text-white my-2  px-4 py-2 text-center flex items-center justify-center focus:outline-none hover:bg-gray-100 hover:text-[#2f6a81] hover:border-[#2f6a81] hover:border-2 mt-3 transition-all duration-300 "
                                        type="submit"
                                        onClick={handleEditProfile}
                                    >
                                        Save Changes
                                    </Button>
                                    <Button
                                        className="rounded-full border-2 w-[140px] border-[#2f6a81] bg-gray-100 text-[#2f6a81] my-2  px-4  py-2 text-center flex items-center justify-center focus:outline-none hover:bg-[#2f6a81] hover:text-white hover:border-white hover:border-2 mt-3 transition-all duration-300"
                                        htmlType="reset"
                                    >
                                        Reset
                                    </Button>
                                </div>
                            </Form>
                        </div>
                    </div>

                </div>
            )}

        </div>
    );
};

export default EditAdminProfile;
