import { Spin, Table } from "antd";
import axios from "axios";
import React, { useEffect, useState } from "react";

const Transaction = () => {
    const token = localStorage.getItem("token");
    const [transaction, setTransaction] = useState([]);
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get(
                    `http://localhost:8080/api/auth/viewAll`,
                    {
                        headers: { Authorization: `Bearer ${token}` },
                    }
                );
                setTransaction(response.data.payload);
                if (response) setIsLoading(true);
            } catch (error) {
                console.error('Error fetching customer list:', error);
            }
        };
        fetchData();
    }, [token]);

    console.log("i", transaction)

    const columns = [
        {
            title: 'Transaction Id',
            dataIndex: 'transactionId',
            key: 'transactionId',
        },
        {
            title: 'User Id',
            dataIndex: 'userId',
            key: 'userId',
        },
        {
            title: 'Transaction Date',
            dataIndex: 'transactionDate',
            key: 'transactionDate',
        },
        {
            title: 'Description',
            dataIndex: 'description',
            key: 'description',
        },
        {
            title: 'TotalMoney',
            dataIndex: 'totalMoney',
            key: 'totalMoney',
            sorter: (a, b) => a.totalMoney - b.totalMoney,

        },
    ];

    const onChange = (sorter) => {
        console.log('params', sorter);
    };

    return (
        <div className='h-full bg-gray-100'>
            <Spin spinning={!isLoading} fullscreen />
            <h1 className='text-center font-semibold text-3xl px-5'>Transaction Management</h1>
            <Table dataSource={transaction} columns={columns} rowKey="transactionId" className='p-5' onChange={onChange} />
        </div>
    );
};

export default Transaction