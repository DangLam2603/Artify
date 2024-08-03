import {
    CloseOutlined,
    EditOutlined,
    PlusOutlined
} from '@ant-design/icons';
import { Avatar, Button, Form, Input, Select, Spin, Switch, Table, Tag } from "antd";
import axios from "axios";
import { Card, Modal } from "flowbite-react";
import React, { useEffect, useState } from "react";
import { AiOutlineSearch } from "react-icons/ai";
import Swal from "sweetalert2";

const formItemCol = {
    labelCol: { span: 24 },
    wrapperCol: { span: 24 },
};
const { Option } = Select;

const Account = () => {
    const token = localStorage.getItem("token");
    const [userList, setUserList] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [openModal, setOpenModal] = useState(false);
    const [userName, setUserName] = useState("");
    const [emailAddress, setEmailAddress] = useState("");
    const [password, setPassword] = useState("");
    const [telephone, setTelephone] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [status, setStatus] = useState("ACTIVE");
    const [roleName, setRoleName] = useState("CREATOR");
    const [files, setFiles] = useState();
    const [searchUserName, setSearchUserName] = useState("");

    const fetchData = async (keyword) => {
        console.log(searchUserName);
        try {
            const response = await axios.get(
                `http://localhost:8080/api/auth/admin/user/search?keyword=${keyword}`,
                {
                    headers: { Authorization: `Bearer ${token}` },
                }
            );
            setUserList(response.data.payload);
            setIsLoading(true);
        } catch (error) {
            console.error('Error fetching user list:', error);
        }
    };

    useEffect(() => {
        fetchData(searchUserName);
    }, [token]);

    const handleAddUser = () => {
        const myHeaders = {
            "Content-Type": "multipart/form-data",
        };
        if (password !== confirmPassword) {
            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: "Confirm password does not match!",
            });
            return;
        } else {
            const addedUser = {
                userName: userName,
                emailAddress: emailAddress,
                telephone: telephone,
                password: password,
                status: status,
                roleName: roleName
            };
            const formData = new FormData();
            if (files != null && files.length > 0) {
                formData.append("image", files[0]);
            }
            console.log(files);
            console.log(addedUser);
            formData.append(
                "user",
                new Blob([JSON.stringify(addedUser)], { type: "application/json" })
            );
            axios
                .post("http://localhost:8080/api/auth/admin/user/add", formData, {
                    headers: { Authorization: `Bearer ${token}`, myHeaders },
                })
                .then((response) => {
                    if (response) setIsLoading(true);
                    if (response.status === 200) {
                        Swal.fire({
                            position: "center",
                            icon: "success",
                            title: `Done`,
                            html: "<h3>Add Successfully</h3>",
                            showConfirmButton: false,
                            timer: 1600,
                        });
                        setOpenModal(false);
                        fetchData(searchUserName);
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

    const columns = [
        {
            title: 'Name',
            dataIndex: 'userName',
            key: 'userName',
        },
        {
            title: 'Email',
            dataIndex: 'emailAddress',
            key: 'emailAddress',
        },
        {
            title: 'Telephone',
            dataIndex: 'telephone',
            key: 'telephone',
        },
        {
            title: 'Status',
            dataIndex: 'status',
            key: 'status',
            render: (text) => (
                <Tag color={(text === 'ACTIVE' || text === 'REQUESTING') ? 'green' : 'red'}>
                    {(text === 'ACTIVE' || text === 'REQUESTING') ? 'ACTIVE' : 'INACTIVE'}
                </Tag>
            ),
            filters: [
                {
                    text: 'ACTIVE',
                    value: 'ACTIVE',
                },
                {
                    text: 'INACTIVE',
                    value: 'INACTIVE',
                },
            ],
            onFilter: (value, record) => record.status.startsWith(value),

        },
        {
            title: 'Role',
            dataIndex: 'roleName',
            key: 'roleName',
            filters: [
                {
                    text: 'AUDIENCE',
                    value: 'AUDIENCE',
                },
                {
                    text: 'CREATOR',
                    value: 'CREATOR',
                },
            ],
            onFilter: (value, record) => record.roleName.startsWith(value),
        },
        {
            title: 'Action',
            key: 'action',
            render: (text, record) => (
                <Switch
                    checkedChildren={record.status === 'ACTIVE' ? 'ACTIVE' : 'INACTIVE'}
                    unCheckedChildren={record.status === 'ACTIVE' ? 'ACTIVE' : 'INACTIVE'}
                    defaultChecked={record.status === 'ACTIVE'}
                    onClick={(checked) => handleChangeStatus(record, checked)}
                    className='w-35px h-30px'
                />
            ),
        },
    ];

    const onChange = (filters) => {
        console.log('params', filters);
    };

    const handleModalOpen = () => {
        setOpenModal(true);
    };
    const handleAvatarChange = (event) => {
        const selectedFile = event.target.files[0];
        if (selectedFile) {
            setFiles([selectedFile]);
        }
    };



    const handleSearchChange = (event) => {
        setSearchUserName(event.target.value);
        fetchData(event.target.value);
    };

    const handleChangeStatus = (record) => {
        const statusToChange = record.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE';
        Swal.fire({
            icon: "warning",
            title: "Warning",
            text: `Do you want to ${statusToChange} the account?`,
        });
    }


    return (
        <>
            <div className='h-full bg-gray-100'>
                <Spin spinning={!isLoading} fullscreen />
                <h1 className='text-center font-semibold text-3xl px-5'>Account Management</h1>

                <div className='flex justify-between mt-10'>
                    <div className='bg-gray-200 rounded-full sm:flex hidden items-center px-4 lg:w-[400px] h-12 mx-5'>
                        <AiOutlineSearch className='cursor-pointer' size={25} style={{ color: '#2f6a81', fontWeight: 'bold' }} />
                        <input
                            className='bg-transparent lg:w-[400px] appearance-none focus:outline-none border-none'
                            type='search'
                            placeholder='Search for account user name...'
                            value={searchUserName}
                            onChange={handleSearchChange}
                        />
                    </div>
                    <div className="mx-5 cursor-pointer w-[150px] h-12 flex justify-center items-center gap-2 text-white bg-[#2f6a81] transition-all duration-300 rounded-lg ">
                        <PlusOutlined className="mr-1" />
                        <button type="submit" onClick={handleModalOpen}>Add Account</button>
                    </div>
                </div>


                <Table dataSource={userList} columns={columns} rowKey="userId" className='p-5' onChange={onChange} pagination={{ pageSize: 7 }} />

                <Modal
                    show={openModal}
                    title="Add Account"
                    className="mt-10 p-20 h-full  zIndex-10 overflow-auto"
                    size={1}
                    onCancel={() => setOpenModal(false)}
                    footer={null}

                >
                    <h1 className="text-center font-semibold text-3xl mt-10">Add account</h1>
                    <p className="text-center mt-3">To add an account, please fill in the fields below.</p>
                    <div className="grid grid-cols-3 gap-10 px-20 py-10">
                        <div className="col-span-1">
                            <Card className="h-[70%]">
                                <div className="flex flex-col items-center">
                                    <div className="relative rounded-full shadow-2xl">
                                        <Avatar
                                            size="large"
                                            style={{ width: '225px', height: '225px' }}
                                            src={
                                                files != null
                                                    ? URL.createObjectURL(files[0])
                                                    : null
                                            }
                                        />
                                        <label
                                            htmlFor="upload"
                                            className="absolute right-0 bottom-1 bg-[#2f6a81] rounded-full p-2 cursor-pointer"
                                        >
                                            <EditOutlined size={20} color="white" />
                                        </label>
                                        <input
                                            type="file"
                                            id="upload"
                                            style={{ display: "none" }}
                                            onChange={handleAvatarChange}
                                        />
                                    </div>
                                </div>
                            </Card>
                        </div>

                        <div className="col-span-2">
                            <div className="" style={{ overflow: 'hidden' }}>
                                <Button
                                    className="absolute top-3 right-3"
                                    type="text"
                                    onClick={() => setOpenModal(false)}
                                    icon={<CloseOutlined />}
                                />

                                <Form
                                    {...formItemCol}
                                    size="large"
                                    style={{
                                        maxWidth: 700,
                                    }}
                                    initialValues={{
                                        remember: true,
                                    }}
                                    autoComplete="off"
                                >
                                    <div className="grid grid-cols-2 gap-3">
                                        <Form.Item
                                            className="mx-0 px-0 w-full"
                                            name="userName"
                                            label="User Name"
                                            rules={[
                                                {
                                                    required: true,
                                                    message: "Please input your username!",
                                                },
                                                {
                                                    pattern: /^[A-Za-z0-9\s]{3,}$/,
                                                    message: "Please input a valid username with at least 3 characters!",
                                                },
                                            ]}
                                        >
                                            <Input
                                                className="w-full px-4  rounded-lg border-[#d9d9d9]"
                                                placeholder="Enter User Name"
                                                onChange={(e) => setUserName(e.target.value)}
                                                name="userName"
                                            />
                                        </Form.Item>

                                        <Form.Item
                                            className="mx-0 px-0 w-full "
                                            name="emailAddress"
                                            label="Email Address"
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
                                                className="w-full px-4 rounded-lg border-[#d9d9d9]"
                                                placeholder="Enter Email Address"
                                                onChange={(e) => setEmailAddress(e.target.value)}
                                                name="emailAddress"
                                            />
                                        </Form.Item>

                                        <Form.Item
                                            className="mx-0 px-0 w-full "
                                            name="password"
                                            label="Password"
                                            rules={[
                                                {
                                                    required: true,
                                                    message: "Please input your password!",
                                                }
                                            ]}
                                        >
                                            <Input.Password
                                                className="w-full px-4  rounded-lg border-[#d9d9d9]"
                                                placeholder="Enter Password"
                                                onChange={(e) => setPassword(e.target.value)}
                                                name="password"
                                            />
                                        </Form.Item>

                                        <Form.Item
                                            className="mx-0 px-0 w-full"
                                            name="confirmPassword"
                                            label="Confirm Password"
                                            rules={[
                                                {
                                                    required: true,
                                                    message: "Please input your confirm password!",
                                                }
                                            ]}
                                        >
                                            <Input.Password
                                                className="w-full px-4  rounded-lg border-[#d9d9d9] "
                                                placeholder="Confirm Password"
                                                onChange={(e) => setConfirmPassword(e.target.value)}
                                                name="confirmPassword"
                                            />
                                        </Form.Item>

                                        <Form.Item
                                            className="mx-0 px-0 w-full "
                                            name="telephone"
                                            label="Telephone"
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
                                                className="w-full px-4 rounded-lg border-[#d9d9d9]"
                                                placeholder="Enter Telephone"
                                                onChange={(e) => setTelephone(e.target.value)}
                                                name="telephone"
                                            />
                                        </Form.Item>
                                        <Form.Item
                                            className="mx-0 px-0 w-full"
                                            name="status"
                                            label="Status"
                                        >
                                            <Select
                                                placeholder="Select status"
                                                onChange={(value) => setStatus(value)}
                                                defaultValue="ACTIVE"
                                            >
                                                <Option value="ACTIVE">Active</Option>
                                                <Option value="INACTIVE">Inactive</Option>
                                            </Select>
                                        </Form.Item>

                                        <Form.Item
                                            className="mx-0 px-0 w-full "
                                            name="roleName"
                                            label="Role"
                                        >
                                            <Select
                                                placeholder="Select role"
                                                onChange={(value) => setRoleName(value)}
                                                defaultValue="AUDIENCE"
                                            >
                                                <Option value="CREATOR">Creator</Option>
                                                <Option value="AUDIENCE">Audience</Option>
                                            </Select>
                                        </Form.Item>


                                        <div className="w-full flex justify-end items-center gap-4">
                                            <Button
                                                className="rounded-full border-2 w-[140px] bg-[#2f6a81] text-white  px-4 py-2 text-center flex items-center justify-center focus:outline-none hover:bg-gray-100 hover:text-[#2f6a81] hover:border-[#2f6a81] hover:border-2 mt-3 transition-all duration-300 "
                                                type="submit" onClick={handleAddUser}
                                            >
                                                Add User
                                            </Button>
                                            <Button
                                                className="rounded-full border-2 w-[140px] border-[#2f6a81] bg-gray-100 text-[#2f6a81]  px-4  py-2 text-center flex items-center justify-center focus:outline-none hover:bg-[#2f6a81] hover:text-white hover:border-white hover:border-2 mt-3 transition-all duration-300"
                                                htmlType="reset"
                                            >
                                                Reset
                                            </Button>
                                        </div>
                                    </div>


                                </Form>
                            </div>
                        </div>

                    </div>
                </Modal>
            </div>
        </>
    );
};

export default Account;