import { Button, Select, Space, Spin, Table, Tag } from "antd";
import axios from "axios";
import React, { useEffect, useState } from "react";
import { AiOutlineSearch } from "react-icons/ai";
import Swal from "sweetalert2";

const formItemCol = {
    labelCol: { span: 24 },
    wrapperCol: { span: 24 },
};
const { Option } = Select;

const CreatorRequest = () => {
    const token = localStorage.getItem("token");
    const [userList, setUserList] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [openModal, setOpenModal] = useState(false);
    const [userName, setUserName] = useState("");
    const [emailAddress, setEmailAddress] = useState("");
    const [password, setPassword] = useState("");
    const [telephone, setTelephone] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [status, setStatus] = useState("");
    const [roleName, setRoleName] = useState("");
    const [files, setFiles] = useState();

    const fetchData = async () => {
        try {
            const response = await axios.get(
                `http://localhost:8080/api/auth/admin/view_audience_request`,
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
        fetchData();
    }, [token]);


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
                <Tag color={text === 'ACTIVE' ? 'green' : 'red'}>
                    {text}
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
                <Space direction="vertical">
                    <Button type="primary" onClick={() => handleAccept(record)}>Accept</Button>
                    <Button type="danger" onClick={() => handleDenied(record)}>Denied</Button>
                </Space>
            ),
        },
    ];

    const onChange = (filters) => {
        console.log('params', filters);
    };

    const handleAccept = (record) => {
        Swal.fire({
            icon: "success",
            title: "Confirmation",
            text: "Do you want to accept?",
            showCancelButton: true,
            confirmButtonText: "Yes, accept",
            cancelButtonText: "Cancel",
        }).then((result) => {
            if (result.isConfirmed) {
                console.log(record.emailAddress);
                axios
                    .post(`http://localhost:8080/api/auth/admin/BecomeCreator?Email=${record.emailAddress}`, null, {
                        headers: { Authorization: `Bearer ${token}` },
                    })
                    .then((response) => {
                        if (response) setIsLoading(true);
                        if (response.status === 200) {
                            Swal.fire({
                                position: "center",
                                icon: "success",
                                title: `Done`,
                                html: "<h3>Accept Successfully</h3>",
                                showConfirmButton: false,
                                timer: 1600,
                            });
                            fetchData();
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
        });
    };


    return (
        <>
            <div className='h-full bg-gray-100'>
                <Spin spinning={!isLoading} fullscreen />
                <h1 className='text-center font-semibold text-3xl px-5'>Creator Request</h1>

                <div className='flex justify-between mt-10'>
                    <div className='bg-gray-200 rounded-full sm:flex hidden items-center px-4 lg:w-[400px] h-12 mx-5'>
                        <AiOutlineSearch className='cursor-pointer' size={25} style={{ color: '#2f6a81', fontWeight: 'bold' }} />
                        <input
                            className='bg-transparent lg:w-[400px] appearance-none focus:outline-none border-none'
                            type='search'
                            placeholder='Search for creator request...'
                        />
                    </div>
                </div>


                <Table dataSource={userList} columns={columns} rowKey="userId" className='p-5' onChange={onChange} />
            </div>

        </>
    );
};

export default CreatorRequest;