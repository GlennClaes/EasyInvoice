import React, {createContext, useEffect} from "react";
import {AppConstants} from "../util/constants.js";
import axios from "axios";
import {toast} from "react-toastify";

export const AppContext = createContext();
export const AppContextProvider = (props) => {

    axios.defaults.withCredentials = true;

    const backendURL = AppConstants.BACKEND_URL;
    const [isLoggedIn, setIsLoggedIn] = React.useState(false);
    const [userData, setUserData] = React.useState(false);

    const getUserData = async () => {
        try{
            const response = await axios.get(backendURL+"/profile");
            if(response.status === 200){
                setUserData(response.data);
            }else {
                toast.error("Unable to retrieve profile");
            }
        }catch(error){
            toast.error(error.message);
        }
    }
    const getAuthState = async () => {
        try {
            const response = await axios.get(backendURL + "/is-authenticated");
            const authenticated = response.data === true || response.data.authenticated === true;

            if (response.status === 200 && authenticated) {
                setIsLoggedIn(true);
                await getUserData();
            } else {
                setIsLoggedIn(false);
            }

        } catch (error) {
            console.log(error);
        }
    };

    useEffect(() => {
        getAuthState();
    }, [])

    const contextValue = {
        backendURL,
        isLoggedIn,setIsLoggedIn,
        userData,setUserData,
        getUserData,
    }

    return (
        <AppContext.Provider value={contextValue}>
            {props.children}
        </AppContext.Provider>
    );
};