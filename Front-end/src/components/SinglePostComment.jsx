import React from "react";
import login from "../assets/login.jpg";

export default function SinglePostComment({
    billyGreenOne ,
    name ,
    time,
    timezone,
    ...props
}) {


    return (
        <div {...props}>
            <div className="flex flex-col items-start justify-start w-full gap-2.5">
                <div className="flex flex-row justify-between items-center w-full mx-auto max-w-[331px]">
                    <div className="flex flex-row justify-start items-center gap-[5px]">
                        <img src={billyGreenOne} alt="billy_green_one" className="w-5 h-5 object-cover rounded-[100%]" />
                        <p className="font-semibold">
                            {name}
                        </p>
                    </div>
                    <p>
                        {time}
                    </p>
                </div>
                <p className="leading-[22px]">
                    {timezone}
                </p>
            </div>
        </div>
    );
}
