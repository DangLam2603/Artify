import {
    AreaChartOutlined,
    LogoutOutlined,
    MenuFoldOutlined,
    MenuUnfoldOutlined,
    ProfileOutlined,
    SendOutlined,
    TransactionOutlined,
    UserOutlined
} from '@ant-design/icons';
import { Button, Layout, Menu } from "antd";
import React, { Fragment, useEffect, useState } from "react";
import logo from "../../assets/logo.png";
import Account from './Account/Account';
import './AdminHome.css';
import AdminProfile from './Profile/AdminProfile';
import Transaction from './Transaction/Transaction';
import CreatorRequest from './CreatorRequest/CreatorRequest';
const { Header, Sider } = Layout;

const AdminHome = () => {
    const [activeComponent, setActiveComponent] = useState('dashboard');
    const [collapsed, setCollapsed] = useState(false);
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const token = localStorage.getItem("token");

    useEffect(() => {
        setIsLoggedIn(token != null);
    }, [token]);

    const handleDashBoardClick = () => {
        setActiveComponent('dashboard');
    };
    const handleAccountClick = () => {
        setActiveComponent('account');
    };
    const handleTransactionClick = () => {
        setActiveComponent('transaction');
    };
    const handleRequestClick = () => {
        setActiveComponent('request');
    };
    const handleProfileClick = () => {
        setActiveComponent('profile');
    };
    const handleLogoutClick = () => {
        localStorage.removeItem('token');
        setIsLoggedIn(false);
        window.location.href = '/';
    };

    const renderActiveComponent = () => {
        if (activeComponent === 'dashboard') {
            return;
        } else if (activeComponent === 'account') {
            return <Account />;
        } else if (activeComponent === 'transaction') {
            return <Transaction />;
        } else if (activeComponent === 'request') {
            return <CreatorRequest />;
        } else if (activeComponent === 'profile') {
            return <AdminProfile />;
        }
    };

    return (
        <Fragment>
            <Layout style={{ height: '100vh' }}>
                <Sider trigger={null} collapsible collapsed={collapsed}>
                    <a href='/home' style={{ display: 'flex', justifyContent: 'center' }}>
                        <img src={logo} className='w-[124px] h-[124px]' alt="Logo" />
                    </a>
                    <Menu
                        theme="dark"
                        className='mt-10'
                        mode="inline"
                        defaultSelectedKeys={['1']}
                        items={[
                            {
                                key: '1',
                                icon: <ProfileOutlined />,
                                label: 'Accounts',
                                style: { fontSize: '1rem', width: '80%', marginLeft: 'auto', marginRight: 'auto', marginBottom: '30px' },
                                onClick: () => handleAccountClick()
                            },
                            {
                                key: '2',
                                icon: <TransactionOutlined />,
                                label: 'Transactions',
                                style: { fontSize: '1rem', width: '80%', marginLeft: 'auto', marginRight: 'auto', marginBottom: '30px' },
                                onClick: () => handleTransactionClick()
                            },
                            {
                                key: '3',
                                icon: <SendOutlined />,
                                label: 'Creator Requests',
                                style: { fontSize: '1rem', width: '80%', marginLeft: 'auto', marginRight: 'auto', marginBottom: '30px' },
                                onClick: () => handleRequestClick()
                            },
                            {
                                key: '4',
                                icon: <UserOutlined />,
                                label: 'Profile',
                                style: { fontSize: '1rem', width: '80%', marginLeft: 'auto', marginRight: 'auto', marginBottom: '30px' },
                                onClick: () => handleProfileClick()
                            },
                            {
                                key: '5',
                                icon: <LogoutOutlined />,
                                label: 'Log Out',
                                style: { fontSize: '1rem', width: '80%', marginLeft: 'auto', marginRight: 'auto', marginBottom: '30px' },
                                onClick: () => handleLogoutClick()
                            },
                        ]}
                    />
                </Sider>
                
                <Layout style={{ zIndex: 0 }}>
                    <Header style={{ padding: 0, backgroundColor: 'whitesmoke' }}>
                        <Button
                            type="text"
                            icon={collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
                            onClick={() => setCollapsed(!collapsed)}
                            style={{
                                fontSize: '16px',
                                width: 64,
                                height: 64,
                            }}
                        />
                        <div className='w-full bg-whitesmoke-100'>
                            {renderActiveComponent()}
                        </div>
                    </Header>
                </Layout>
            </Layout>
        </Fragment >
    )
}

export default AdminHome;