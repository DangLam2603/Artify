import React, { useState, useEffect } from "react";
import NavBar from "../../components/NavBar";
import axios from "axios";
import { Table } from "flowbite-react";
import { Modal } from "flowbite-react";
import { Formik, Form, Field } from "formik";
import { MdOutlineModeEdit } from "react-icons/md";
import { Button, Upload, Avatar, Spin } from "antd";
import Swal from "sweetalert2";
import { useNavigate } from "react-router-dom";

const ReturnPreOrder = () => {
  const [waitingList, setWaitingList] = useState([]);
  const token = localStorage.getItem("token");
  const [openModal, setOpenModal] = useState(false);
  const [files, setFiles] = useState();
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  const handleAvatarChange = (event) => {
    const selectedFile = event.target.files[0];
    if (selectedFile) {
      setFiles([selectedFile]);
    }
  };


  useEffect(() => {
    fetchData();
  }, []);

  // useEffect(() => {
    const fetchData = async () => {
      const result = await axios.get(
        "http://localhost:8080/api/auth/creator/viewProcessedPreOrders",
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      setWaitingList(result.data.payload);
      if (result) setIsLoading(true);
    };
    fetchData();
  // }, []);

  const handleOpenAccept = () => {
    setOpenModal(true);
  };

  const handleAccept = async (values) => {
    console.log(values);
    const formData = new FormData();
    if (files != null && files.length > 0) {
      formData.append("preOrderArtWork", files[0]);
    }
    formData.append(
      "updatedPreOrder",
      new Blob([JSON.stringify(values)], { type: "application/json" })
    );
    const result = await axios.put(
      "http://localhost:8080/api/auth/creator/updatePreOrder",
      formData,
      {
        headers: { Authorization: `Bearer ${token}` },
      }
    );

    console.log(files);
    console.log(result);
    if (result.status === 200) {
      Swal.fire({
        position: "center",
        icon: "success",
        title: `Done`,
        html: "<h3>Update Successfully</h3>",
        showConfirmButton: false,
        timer: 1600,
      });
      navigate("/returnPreOrderArt");
    }
  };


  return (
    <div className="w-full h-full bg-gray-100">
      <Spin spinning={!isLoading} fullscreen />
      <NavBar />
      <div className="h-full p-28 mt-5">
        <h1 className="text-center text-3xl font-semibold mb-10 text-[#2f6a81]">
          Your request orders
        </h1>
        <div className="overflow-x-auto">
          <Table striped>
            <Table.Head>
              <Table.HeadCell>No</Table.HeadCell>
              <Table.HeadCell>Creator</Table.HeadCell>
              <Table.HeadCell>Pre-order Date</Table.HeadCell>
              <Table.HeadCell>Requirement</Table.HeadCell>
              <Table.HeadCell>Price</Table.HeadCell>
              <Table.HeadCell>Status</Table.HeadCell>
              <Table.HeadCell>Creator Note</Table.HeadCell>
              <Table.HeadCell>
                <span className="sr-only">Cancel</span>
              </Table.HeadCell>
            </Table.Head>
            <Table.Body className="divide-y">
              {waitingList.length > 0 &&
                waitingList.map((item, index) => (
                  <Table.Row
                    key={item.preOrderId}
                    className="bg-white dark:border-gray-700 dark:bg-gray-800"
                  >
                    <Table.Cell className="whitespace-nowrap font-medium text-gray-900 dark:text-white">
                      {index + 1}
                    </Table.Cell>
                    <Table.Cell>{item.creatorName}</Table.Cell>
                    <Table.Cell>{item.preOrderDate}</Table.Cell>
                    <Table.Cell>{item.requirement}</Table.Cell>
                    <Table.Cell>{item.price}</Table.Cell>
                    <Table.Cell>{item.status}</Table.Cell>
                    <Table.Cell>{item.creatorNote}</Table.Cell>
                    <Table.Cell>
                      <a
                        onClick={handleOpenAccept}
                        href="#"
                        className="font-medium text-cyan-600 hover:underline dark:text-cyan-500"
                      >
                        Add Art
                      </a>
                    </Table.Cell>
                    <Modal
                      dismissible
                      className="mt-10 px-72"
                      size={1}
                      show={openModal}
                      // onClose={() => setOpenModal(false)}
                    >
                      <Modal.Body className="flex justify-center items-center">
                        <Formik
                          initialValues={{
                            preOrderId: item.preOrderId,
                            creatorNote: "",
                            status: "confirming",
                            price : item.price
                          }}
                          // validationSchema={validationSchema}
                          onSubmit={handleAccept}
                        >
                          {({ values, setFieldValue }) => (
                            <Form>
                              <Field
                                name="creatorNote"
                                type="text"
                                className="rounded-lg w-full border-[#d9d9d9] mb-5"
                                placeholder="Your note"
                              />
                              <div className="flex flex-col items-center">
                                <div className="relative rounded-full shadow-2xl">
                                  <Avatar
                                    size="large"
                                    style={{ width: "225px", height: "225px" }}
                                    src={
                                      files != null &&
                                      URL.createObjectURL(files[0])
                                    }
                                    shape="square"
                                  />
                                  <label
                                    htmlFor="upload"
                                    className="absolute right-0 bottom-1 bg-[#2f6a81] rounded-full p-1.5 cursor-pointer"
                                  >
                                    <MdOutlineModeEdit
                                      size={20}
                                      color="white"
                                    />
                                  </label>
                                  <input
                                    type="file"
                                    id="upload"
                                    style={{ display: "none" }}
                                    onChange={handleAvatarChange}
                                  />
                                </div>
                              </div>

                              <button type="submit">Send</button>
                            </Form>
                          )}
                        </Formik>
                      </Modal.Body>
                    </Modal>
                  </Table.Row>
                ))}
            </Table.Body>
          </Table>
        </div>
      </div>
    </div>
  );
};

export default ReturnPreOrder;
