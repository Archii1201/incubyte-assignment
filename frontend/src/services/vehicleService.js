import api from "../api/api";

export const createVehicle = async (vehicle) => {

    const response = await api.post(
        "/vehicles",
        vehicle
    );

    return response.data;
};