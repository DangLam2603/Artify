import React, { useState, useEffect } from "react";
import NavBar from "../../components/NavBar";
import axios from "axios";
import { Table } from "flowbite-react";
import { Modal } from "flowbite-react";
import { Formik, Form, Field } from "formik";
import { Link } from "react-router-dom";

const ViewPreOrders = () => {
  const [waitingList, setWaitingList] = useState([]);
  const [openModal, setOpenModal] = useState(false);
  const [files, setFiles] = useState();
  const [preOrderDTO, setPreOrderDTO] = useState({
    note: "",
    price: 0,
    status: "",
  });
  const handleAvatarChange = (event) => {
    const selectedFile = event.target.files[0];
    if (selectedFile) {
      setFiles([selectedFile]);
    }
  };
  const [btnCheck, setBtnCheck] = useState("accept");
  const token = localStorage.getItem("token");
  useEffect(() => {
    const fetchData = async () => {
      const result = await axios.get(
        "http://localhost:8080/api/auth/creator/viewPendingCreatorPreOrders",
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      setWaitingList(result.data.payload);
    };
    fetchData();
  }, []);

  console.log(waitingList)
  const handleOpenAccept = () => {
    setOpenModal(true);
    setBtnCheck("accept");
  };

  const handleOpenDenied = () => {
    setOpenModal(true);
    setBtnCheck("denied");
  };
  const handleAccept = async (values) => {
    console.log(values);
    const formData = new FormData();
    // if(files != null) formData.append("preOrderArtWork", files);
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
    console.log(result);
  };
  const handleDenied = (values) => {
    console.log(values);
  };
  return (
    <div className="w-full h-full bg-gray-100">
      <NavBar />
      <div className="h-full p-28 mt-5">
        <h1 className="text-center text-3xl font-semibold mb-10 text-[#2f6a81]">
          Request History
        </h1>
      <Link to="/returnPreOrderArt">Process</Link>

        <div className="overflow-x-auto">
          <Table striped>
            <Table.Head>
              <Table.HeadCell>No</Table.HeadCell>
              <Table.HeadCell>Creator</Table.HeadCell>
              <Table.HeadCell>Pre-order Date</Table.HeadCell>
              <Table.HeadCell>Requirement</Table.HeadCell>
              <Table.HeadCell>Status</Table.HeadCell>
              <Table.HeadCell>
                <span className="sr-only">Cancel</span>
              </Table.HeadCell>
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
                    <Table.Cell>{item.status}</Table.Cell>
                    <Table.Cell>
                      <a
                        href="#"
                        onClick={handleOpenAccept}
                        className="font-medium text-cyan-600 hover:underline dark:text-cyan-500"
                      >
                        Accept Request
                      </a>
                    </Table.Cell>
                    <Table.Cell>
                      <a
                        href="#"
                        className="font-medium text-cyan-600 hover:underline dark:text-cyan-500"
                        onClick={handleOpenDenied}
                      >
                        Denied Request
                      </a>
                    </Table.Cell>
                    {btnCheck === "accept" ? (
                    <Modal
                      dismissible
                      className="mt-10 px-72"
                      size={1}
                      show={openModal}
                      onClose={() => setOpenModal(false)}
                    >
                      <Modal.Body className="flex justify-center items-center">
                        <Formik
                          initialValues={{
                            preOrderId : item.preOrderId,
                            creatorNote: "",
                            price: 0,
                            status: "accepted"
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
                              <Field
                                name="price"
                                type="number"
                                className="rounded-lg w-full border-[#d9d9d9] mb-5"
                                placeholder="price"
                              />
                              {/* <div className="flex flex-col items-center">
                                <div className="relative rounded-full shadow-2xl">
                                  <Avatar
                                    size="large"
                                    style={{ width: "225px", height: "225px" }}
                                    src={
                                      files != null && URL.createObjectURL(files[0])
                                    }
                                    shape="square"
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
                              </div> */}
      
                              <button type="submit">Send</button>
                            </Form>
                          )}
                        </Formik>
                      </Modal.Body>
                    </Modal>
                  ) : (
                    <Modal
                      dismissible
                      className="mt-10 px-72"
                      size={1}
                      show={openModal}
                      onClose={() => setOpenModal(false)}
                    >
                      <Modal.Body className="flex justify-center items-center">
                        <Formik
                          initialValues={{
                            creatorNote: "",
                            status : "denied"
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
                              <button type="submit">Send</button>
                            </Form>
                          )}
                        </Formik>
                      </Modal.Body>
                    </Modal>
                  )}
                  </Table.Row>
                  
                ))}
            </Table.Body>
           
          </Table>
        </div>
      </div>
    </div>
  );
};

export default ViewPreOrders;
