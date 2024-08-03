import React from "react";
import { RouterProvider, createBrowserRouter } from 'react-router-dom';
import EditAdminProfile from "./pages/Admin/Profile/EditAdminProfile";
import AddMoneyInput from "./pages/EWallet/AddMoneyInput";
import AddSuccess from "./pages/EWallet/AddSuccess";
import ReturnWallet from "./pages/EWallet/ReturnWallet";
import ViewEwallet from "./pages/EWallet/ViewEwallet";
import Gallery from "./pages/Gallery/Gallery";
import Home from "./pages/Home/Home";
import Login from "./pages/Login/Login";
import AddArts from "./pages/Post/AddArts";
import UpdatePost from "./pages/Post/UpdatePost";
import ArtistList from "./pages/Profile/ArtistList";
import ArtistProfile from "./pages/Profile/ArtistProfile";
import EditProfile from "./pages/Profile/EditProfile";
import Register from "./pages/Register/Register";
import ConfirmPreOrder from "./pages/Request/ConfirmPreOrder";
import ProcessPreOrder from "./pages/Request/ProcessPreOrder";
import RequestHistory from "./pages/Request/RequestHistory";
import ReturnPreOrder from "./pages/ReturnOrderRequest/ReturnPreOrder";
import ViewPreOrders from "./pages/ReturnOrderRequest/ViewPreOrders";
import SinglePost from "./pages/SinglePost/SinglePost";
import PreviewPost from "./pages/Post/PreviewPost";


const router = createBrowserRouter([
  {
    path: "/",
    element: <div><Login /></div>,
  },
  {
    path: "/register",
    element: <div><Register /></div>,
  },
  {
    path: "/home",
    element: <div><Home /></div>,
  },
  {
    path: "/singlePost",
    element: <div><SinglePost /></div>,
  },
  {
    path: "/viewEwallet",
    element: <div><ViewEwallet /></div>,
  },
  {
    path: "/returnEwallet",
    element: <div><ReturnWallet /></div>,
  },
  {
    path: "/addInputMoney",
    element: <div><AddMoneyInput /></div>,
  },
  {
    path: "/addSuccess",
    element: <div><AddSuccess /></div>,
  },
  {
    path: "/artistProfile",
    element: <div><ArtistProfile /></div>,
  },
  {
    path: "/addArts",
    element: <div><AddArts /></div>,
  },
  {
    path: "/gallery",
    element: <div><Gallery /></div>,
  },
  {
    path: "/requestHistory",
    element: <div><RequestHistory /></div>,
  },
  {
    path: "/editProfile",
    element: <div><EditProfile /></div>,
  },
  {
    path: "/viewPreordersByCreator",
    element: <div><ViewPreOrders /></div>,
  },
  {
    path: "/artistList",
    element: <div><ArtistList /></div>,
  },
  {
    path: "/processRequest",
    element: <div><ProcessPreOrder /></div>,
  },
  {
    path: "/editAdminProfile",
    element: <div><EditAdminProfile /></div>,
  },
  {
    path: "/returnPreOrderArt",
    element: <div><ReturnPreOrder /></div>,
  },
  {
    path: "/getPreOrderArt",
    element: <div><ConfirmPreOrder /></div>,
  },
  {
    path: "/viewMyPosts",
    element: <div><ArtistProfile /></div>,
  },
  {
    path: "/updatePost",
    element: <div><UpdatePost /></div>,
  },
  {
    path: "/previewPost",
    element: <div><PreviewPost /></div>,
  }
]);

function App() {
  return (
    <>
      <RouterProvider router={router} />
    </>
  )
}

export default App
