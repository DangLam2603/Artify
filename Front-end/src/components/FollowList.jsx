import React from 'react'
import { Card } from 'flowbite-react';
import { Badge } from 'flowbite-react';

const FollowList = ({ follow }) => {
    return (
        <div className='flex justify-center pb-10' >
            <Card className="w-1/2">
                <div className="mb-4 flex items-center justify-center ">
                    <h5 className="text-xl font-bold leading-none text-gray-900 dark:text-white">Followers</h5>
                </div>
                <div className="flow-root">
                    {follow?.length > 0
                        ? follow?.map((f,index) => (
                            < ul key={index} className="divide-y divide-gray-200 dark:divide-gray-700">
                                <li className="py-3 sm:py-4">
                                    <div className="flex items-center space-x-4">
                                        <div className="shrink-0">
                                            <img
                                                alt="Avatar"
                                                height="32"
                                                src={f.imagePath}
                                                width="32"
                                                className="rounded-full"
                                            />
                                        </div>
                                        <div className="min-w-0 flex-1">
                                            <p className="truncate text-sm font-medium text-gray-900 dark:text-white">{f.userName}</p>
                                            <p className="truncate text-sm text-gray-500 dark:text-gray-400">{f.emailAddress}</p>
                                        </div>
                                        <div className="inline-flex items-center text-base font-semibold text-gray-900 dark:text-white capitalize">
                                            <Badge color="pink">{f.roleName}</Badge>
                                        </div>
                                    </div>
                                </li>
                            </ul>
                        ))
                        : null}
                </div>
            </Card >
        </div >
    )
}

export default FollowList