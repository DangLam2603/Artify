import React, { useState, useEffect } from "react";
import { Formik, Form, Field, FieldArray, ErrorMessage } from "formik";
import * as Yup from "yup";
import { Avatar, Card, Footer } from "flowbite-react";
import { Carousel } from "flowbite-react";
import { IoIosPerson } from "react-icons/io";
import axios from "axios";
import NavBar from "../../components/NavBar";
import {
  UploadOutlined,
  PlusOutlined,
  LoadingOutlined
} from "@ant-design/icons";
import { Button, Upload } from "antd";
import { message, Steps, theme } from "antd";
import { Watermark,Spin } from "antd";
import { Select } from "antd";
import { useNavigate } from "react-router-dom";
import FooterPart from '../../components/FooterPart'
import Swal from "sweetalert2";


const AddArts = () => {
  const [selectedFiles, setSelectedFiles] = useState([]);
  const [current, setCurrent] = useState(0);
  const [loading, setLoading] = useState(false);
  const [imageUrl, setImageUrl] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const navigate = useNavigate();
  const steps = [
    {
      title: "First",
      content: "First-content",
    },
    {
      title: "Second",
      content: "Second-content",
    }
  ];

  const next = () => {
    setCurrent(current + 1);
  };

  const prev = () => {
    setCurrent(current - 1);
  };

  const items = steps.map((item) => ({ key: item.title, title: item.title }));
  const handleBeforeUpload = (file) => {
    setSelectedFiles([file]);
    return true;
  };

  const getBase64 = (img, callback) => {
    const reader = new FileReader();
    reader.addEventListener("load", () => callback(reader.result));
    reader.readAsDataURL(img);
  };
  const [fileSelectedMessage, setFileSelectedMessage] = useState("");
  const handleFileChange = (info) => {
    if (info.file.status === "uploading") {
      setLoading(true);
      return;
    }
    if (info.file.status === "done") {
      getBase64(info.file.originFileObj, (url) => {
        setLoading(false);
        setImageUrl(url);
      });
    }
    if (info.fileList.length > 0) {
      setFileSelectedMessage(`File selected: ${info.fileList[0].name}`);
    } else {
      setFileSelectedMessage("");
    }
  };

  console.log(imageUrl);
  const handleRemoveArtwork = (index) => {
    setSelectedFiles((prevFiles) => {
      const newFiles = [...prevFiles];
      newFiles.splice(index, 1);
      return newFiles;
    });
  };

  const validationSchema = Yup.object().shape({
    description: Yup.string().required("Description is required"),
    artList: Yup.array().of(
      Yup.object().shape({
        artName: Yup.string().required("Art name is required"),
        imagePath: Yup.string().required("Image path is required"),
        price: Yup.number()
          .positive("Price must be positive")
          .required("Price is required"),
        type: Yup.string().required("Type is required"),
      })
    ),
  });
  const token = localStorage.getItem("token");
  const myHeaders = new Headers();
  myHeaders.append("Authorization", `Bearer ${token}`);
  console.log(token);

  const handleSubmit = async (values) => {
    const formData = new FormData();

    const fileArray = Array.from(selectedFiles);
    const updatedValue = {
      ...values,
      artList: values.artList.map((art) => ({ ...art, imageUrl: null })),
    };
    values.artList.forEach((art) => {
      if (art.imageFile) {
        fileArray.push(art.imageFile);
      }
    });
    fileArray.forEach((file) => {
      formData.append("image", file);
    });

    formData.append(
      "post",
      new Blob([JSON.stringify(updatedValue)], { type: "application/json" })
    );
    console.log(updatedValue);
    console.log(selectedFiles);
    const response = await axios.post(
      "http://localhost:8080/api/auth/creator/addPost",
      formData,
      {
        headers: { Authorization: `Bearer ${token}` },
      }
    );
    console.log(response.data);
    if(response.status === 200) {
      Swal.fire({
        position: "center",
        icon: "success",
        title: `Done`,
        html: "<h3>Update Successfully</h3>",
        showConfirmButton: false,
        timer: 1600,
      });
      navigate(`/previewPost?postId=${response.data.payload.postId}`)
      setIsLoading(true);
    }
  };

  const uploadButton = (
    <button style={{ border: 0, background: "none" }} type="button">
      {loading ? <LoadingOutlined /> : <PlusOutlined />}
      <div style={{ marginTop: 8 }}>Upload</div>
    </button>
  );

  return (
    <div>
      <NavBar />
      <div className="bg-gray-100 mx-auto max-w-screen-xl p-4">
        <div className="bg-gray-100 mt-40 min-h-screen">
          <h1 className="text-3xl text-[#2f6a81] text-center font-semibold m-5">
            Add Post
          </h1>
          <Spin fullscreen spinning={isLoading}/>
          <Steps
            current={current}
            items={items}
            className="mx-auto max-w-4xl py-24 sm:py-8"
          />
          <div
            className="flex items-center justify-center"
            style={{ marginBottom: "-20px" }}
          >
            <Avatar size="large" icon={<IoIosPerson />} />
          </div>

          <div className="flex bg-slate-200 rounded-md flex-col mx-auto max-w-4xl py-24 sm:py-8 justify-center items-center">
            <Formik
              initialValues={{
                description: "",
                artList: [
                  {
                    artName: "",
                    imagePath: "",
                    price: 0,
                    type: "Free",
                    imageUrl: "",
                    imageFile: null,
                  },
                ],
              }}
              validationSchema={validationSchema}
              onSubmit={handleSubmit}
            >
              {({ values, setFieldValue }) => (
                <Form className="w-full px-32">
                  {current == 0 ? (
                    <div>
                      <Field
                        type="text"
                        name="description"
                        className="bg-slate-200 w-full rounded-lg"
                        autocomplete="off"
                        placeholder="Description..."
                      />
                      <ErrorMessage
                        name="description"
                        component="div"
                        className="text-red-500"
                      />
                    </div>
                  ) : current == 1 ? (
                    <FieldArray name="artList">
                      {({ push, remove }) => (
                        <div>
                          <h3 className="text-3xl text-[#2f6a81] text-center font-semibold mb-5">Art List</h3>
                          {values.artList.map((artwork, index) => (
                            <div key={index} className="w-full">
                              <div className="flex justify-between mb-5 mt-10">
                                <label className="font-semibold" htmlFor={`artList.${index}.artName`}>
                                  Art Name
                                </label>
                                <Field
                                  type="text"
                                  name={`artList.${index}.artName`}
                                  className="rounded-lg w-[80%] h-8 border-[#d9d9d9] mb-5"
                                />
                              </div>
                              <ErrorMessage
                                name={`artList.${index}.artName`}
                                component="div"
                                className="text-red-500"
                              />
                              <div>
                                <Upload
                                  action="https://run.mocky.io/v3/435e224c-44fb-4773-9faf-380c5e6a2188"
                                  name="avatar"
                                  listType="picture-card"
                                  className="avatar-uploader mb-5"
                                  showUploadList={false}
                                  beforeUpload={(file) => {
                                    setFieldValue(
                                      `artList.${index}.imageFile`,
                                      file
                                    );
                                    return true;
                                  }}
                                  onChange={(info) => {
                                    if (info.file.status === "uploading") {
                                      setLoading(true);
                                      return;
                                    }
                                    if (info.file.status === "done") {
                                      getBase64(
                                        info.file.originFileObj,
                                        (url) => {
                                          setLoading(false);
                                          setFieldValue(
                                            `artList.${index}.imageUrl`,
                                            url
                                          );
                                        }
                                      );
                                    }
                                  }}
                                >
                                  {values.artList[index].imageUrl ? (
                                    <img
                                      src={values.artList[index].imageUrl} // Sử dụng imageUrl tương ứng với art hiện tại
                                      alt="avatar"
                                      style={{ width: "100%" }}
                                    />
                                  ) : (
                                    uploadButton
                                  )}
                                </Upload>
                                {values.artList[index].imageFile ? <div></div> : <div>Please add file</div>}
                              </div>

                              <div className="flex justify-between">
                                <label className="font-semibold" htmlFor={`artList.${index}.type`}>
                                  Type
                                </label>
                                <Select
                                  defaultValue="Free"
                                  className="rounded-lg w-[80%] border-[#d9d9d9] mb-5"
                                  name={`artList.${index}.type`}
                                  options={[
                                    { value: "Free", label: "Free" },
                                    { value: "Premium", label: "Premium" },
                                  ]}
                                  value={values.artList[index].type}
                                  onChange={(value) =>
                                    setFieldValue(
                                      `artList.${index}.type`,
                                      value
                                    )
                                  }
                                />
                              </div>

                              {values.artList[index].type == "Premium" && (
                                <div className="flex justify-between">
                                  <label htmlFor={`artList.${index}.price`}>
                                    Price
                                  </label>
                                  <Field
                                    type="number"
                                    name={`artList.${index}.price`}
                                    className="rounded-lg w-[80%] border-[#d9d9d9] "
                                  />
                                </div>
                              )}

                              <div className="flex justify-end">
                                <div className="cursor-pointer w-[20%] sm:flex gap-2 hidden items-center justify-center text-white bg-red-400 px-4 py-1 transition-all duration-300 rounded-full ">
                                  <button
                                    type="button"
                                    onClick={() => {
                                      remove(index);
                                      handleRemoveArtwork(index);
                                    }}
                                  >
                                    Remove
                                  </button>
                                </div>
                              </div>

                            </div>
                          ))}
                          <div className="mt-5 cursor-pointer w-[25%] sm:flex gap-2 hidden items-center justify-center text-white bg-[#2f6a81] px-4 py-2 transition-all duration-300 rounded-full my-1 mx-auto"
                          >
                            <PlusOutlined />
                            <button
                              type="button"
                              onClick={() =>
                                push({
                                  artName: "",
                                  imagePath: "",
                                  price: 0,
                                  type: "Free",
                                })
                              }
                            >

                              Add Artwork
                            </button>
                          </div>



                        </div>
                      )}
                    </FieldArray>
                  ) : (
                    <div className="w-full flex flex-col justify-center items-center">
                      <Card className="w-[700px] justify-center flex bg-white shadow-md shadow-gray-300 rounded-md mb-5 w-1/2">
                        <div className="flex justify-between gap-3">
                          <Avatar rounded>
                            <div className="space-y-1 dark:text-white">
                              {/* <div className='font-medium'>{p.creatorName}</div> */}
                              {/* <div className="text-sm text-gray-500 dark:text-gray-400">{p.artList.map(item => item.createdDate)}</div> */}
                            </div>
                          </Avatar>
                        </div>

                        <div>
                          <p className="w-full text-sm my-2">{values.description}</p>
                          <div className="w-[700px] h-screen max-h-[50vh]">
                            <Carousel pauseOnHover className="w-full mx-auto">
                              {values.artList.map((item, index) => (
                                <div key={index}>
                                  {item.type === "Free" ? (
                                    <img
                                      src={item.imageUrl}
                                      className="rounded-md w-[700px] mx-auto"
                                      alt={`Post Image - ${values.description}`}
                                    />
                                  ) : (
                                    <Watermark
                                      content="Ant Design"
                                      width={1200}
                                      height={64}
                                    >
                                      <img
                                        src={item.imageUrl}
                                        className="rounded-md w-[700px] mx-auto"
                                        alt={`Post Image - ${values.description}`}
                                      />
                                    </Watermark>
                                  )}
                                </div>
                              ))}
                            </Carousel>
                          </div>
                        </div>

                        <div className="mt-1 flex justify-between">
                          <div className="flex justify-between gap-16">
                            <button className="flex gap-2 items-center">
                              <svg
                                xmlns="http://www.w3.org/2000/svg"
                                fill="none"
                                viewBox="0 0 24 24"
                                strokeWidth={1.5}
                                stroke="currentColor"
                                className="w-6 h-6"
                              >
                                <path
                                  strokeLinecap="round"
                                  strokeLinejoin="round"
                                  d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z"
                                />
                              </svg>
                              {/* {p.numberOfLikes} */}
                            </button>
                            <button className="flex gap-2 items-center">
                              <svg
                                xmlns="http://www.w3.org/2000/svg"
                                fill="none"
                                viewBox="0 0 24 24"
                                strokeWidth={1.5}
                                stroke="currentColor"
                                className="w-6 h-6"
                              >
                                <path
                                  strokeLinecap="round"
                                  strokeLinejoin="round"
                                  d="M8.625 12a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Zm0 0H8.25m4.125 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Zm0 0H12m4.125 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Zm0 0h-.375M21 12c0 4.556-4.03 8.25-9 8.25a9.764 9.764 0 0 1-2.555-.337A5.972 5.972 0 0 1 5.41 20.97a5.969 5.969 0 0 1-.474-.065 4.48 4.48 0 0 0 .978-2.025c.09-.457-.133-.901-.467-1.226C3.93 16.178 3 14.189 3 12c0-4.556 4.03-8.25 9-8.25s9 3.694 9 8.25Z"
                                />
                              </svg>
                              20
                            </button>
                          </div>

                          <button className="flex gap-2 items-center">
                            <svg
                              xmlns="http://www.w3.org/2000/svg"
                              fill="none"
                              viewBox="0 0 24 24"
                              strokeWidth={1.5}
                              stroke="currentColor"
                              className="w-6 h-6"
                            >
                              <path
                                strokeLinecap="round"
                                strokeLinejoin="round"
                                d="M7.217 10.907a2.25 2.25 0 1 0 0 2.186m0-2.186c.18.324.283.696.283 1.093s-.103.77-.283 1.093m0-2.186 9.566-5.314m-9.566 7.5 9.566 5.314m0 0a2.25 2.25 0 1 0 3.935 2.186 2.25 2.25 0 0 0-3.935-2.186Zm0-12.814a2.25 2.25 0 1 0 3.933-2.185 2.25 2.25 0 0 0-3.933 2.185Z"
                              />
                            </svg>
                            20
                          </button>
                        </div>
                      </Card>
                    </div>
                  )}
                  <div className="cursor-pointer w-[20%] mx-auto mt-5 sm:flex gap-2 hidden items-center justify-center text-white bg-[#2f6a81] px-4 py-2 transition-all duration-300 rounded-full ">
                    <button type="submit">Submit</button>
                  </div>
                </Form>
              )}
            </Formik>
          </div>


          <div className="flex justify-center mt-5">

            {current < steps.length - 1 && (
              <Button onClick={() => next()}>Next</Button>
            )}


            {current > 0 && (
              <Button style={{ margin: "0 8px" }} onClick={() => prev()}>
                Previous
              </Button>
            )}
          </div>

        </div>
      </div>
      <FooterPart />
    </div>
  );
};

export default AddArts;
