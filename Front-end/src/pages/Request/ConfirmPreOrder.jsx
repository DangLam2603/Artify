import React, { useState, useEffect } from "react";
import NavBar from "../../components/NavBar";
import { Table } from "flowbite-react";
import { Avatar, Card, Modal } from "flowbite-react";
import axios from "axios";
import { Formik, Form, Field } from "formik";
import FooterPart from "../../components/FooterPart";
import { saveAs } from "file-saver";
import { MdOutlineFileDownload } from "react-icons/md";
import { Rate } from "antd";

const ConfirmPreOrder = () => {
  const token = localStorage.getItem("token");
  const [openModal, setOpenModal] = useState(false);
  const [requestList, setRequestList] = useState([]);
  const [selectedOrder, setSelectedOrder] = useState(null);
  useEffect(() => {
    const fetchData = async () => {
      const result = await axios.get(
        "http://localhost:8080/api/auth/viewConfirmingPreOrders",
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      setRequestList(result.data.payload);
    };
    fetchData();
  }, []);
  const downloadArt = async (image) => {
    const response = await axios.get(
      `http://localhost:8080/api/auth/downloadArt?artId=${image.artworkId}`
    );
    saveAs(`${response.data.payload}`, `${image.artName}.jpg`);
  };
  console.log(requestList);

  const [rating, setRating] = useState(0);

  const handleRatingChange = (value) => {
    setRating(value);
  };
  const openCompleteOrder = (item) => {
    setOpenModal(true);
    setSelectedOrder(item);
  };

  const handleConfirm = async (values) => {
    const result = await axios.put(
      "http://localhost:8080/api/auth/audience/complete",
      values,
      {
        headers: { Authorization: `Bearer ${token}` },
      }
    );
    console.log(result);
    console.log(values);
  };

  return (
    <div className="w-full h-full bg-gray-100">
      <NavBar />
      <div className="h-full p-28 mt-5">
        <h1 className="text-center text-3xl font-semibold mb-10 text-[#2f6a81]">
          Confirm your Arts
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
              <Table.HeadCell>Art image</Table.HeadCell>
              <Table.HeadCell>Creator Note</Table.HeadCell>
              <Table.HeadCell>
                <span className="sr-only">Accept</span>
              </Table.HeadCell>
              <Table.HeadCell>
                <span className="sr-only">Cancel</span>
              </Table.HeadCell>
            </Table.Head>
            <Table.Body className="divide-y">
              {requestList.length > 0 &&
                requestList.map((item, index) => (
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
                    <Table.Cell>
                      <img src={item.artworkImagePath} className="w-[700px]" />
                    </Table.Cell>
                    <Table.Cell>{item.creatorNote}</Table.Cell>
                    <Table.Cell>
                      <a
                        onClick={() => openCompleteOrder(item)}
                        href="#"
                        className="font-medium text-cyan-600 hover:underline dark:text-cyan-500"
                      >
                        Confirm
                      </a>
                    </Table.Cell>
                  </Table.Row>
                ))}
            </Table.Body>
          </Table>
        </div>
      </div>
      {selectedOrder !== null && (
        <Modal
          dismissible
          className="mt-10 px-72"
          size={1}
          show={openModal}
          onClose={() => setOpenModal(false)}
        >
          <Modal.Body className="flex flex-col justify-center items-center">
            <img
              src={selectedOrder.artworkImagePath}
              className="w-[600px] h-[380px] mx-auto relative"
            />
            <div
              onClick={() => downloadArt(selectedOrder)}
              className="cursor-pointer sm:flex gap-2 hidden items-center text-white bg-[#2f6a81] px-4 transition-all duration-300 rounded-full my-1"
            >
              <MdOutlineFileDownload
                size={20}
                style={{ color: "#fff", fontWeight: "bold" }}
              />
              <button type="submit">Download</button>
            </div>
            <Formik
              initialValues={{
                preOrderId: selectedOrder.preOrderId,
                audienceRating: 0,
                audienceFeedback: "",
              }}
              onSubmit={handleConfirm}
            >
              {({ values, setFieldValue }) => (
                <Form>
                  <Rate
                    name="audienceRating"
                    value={values.audienceRating}
                    onChange={(value) => setFieldValue("audienceRating", value)}
                  />
                  <Field
                    type="text"
                    name="audienceFeedback"
                    className="bg-slate-200 w-full rounded-lg"
                    autocomplete="off"
                    placeholder="Feedback..."
                  />
                  <button type="submit">Confirm</button>
                </Form>
              )}
            </Formik>
          </Modal.Body>
        </Modal>
      )}
      <FooterPart />
    </div>
  );
};

export default ConfirmPreOrder;
