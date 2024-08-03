import React, { useState } from "react";
import { Modal } from "flowbite-react";
import { AiOutlineClose } from "react-icons/ai";

const ModalComponent = ({ isOpen, onClose, children }) => {
  return (
    <Modal show={isOpen} onClose={onClose}>
      <div className="relative">
        <button
          onClick={onClose}
          className="absolute top-2 right-2 text-gray-500 hover:text-gray-800 focus:outline-none"
        >
          <AiOutlineClose size={24} />
        </button>
      </div>
      {children}
    </Modal>
  );
};

export default ModalComponent;
