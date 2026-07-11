import api from "../api/api";

export const createVehicle = async (vehicle) => {

    const response = await api.post(
        "/vehicles",
        vehicle
    );

    return response.data;
};
export const getVehicles = async () => {

    const response =
        await api.get("/vehicles");

    return response.data;
};
export const updateVehicle = async (id, vehicle) => {
    const response = await api.put(`/vehicles/${id}`, vehicle);
    return response.data;
};

export const getVehicleById = async (id) => {
    const response = await api.get(`/vehicles/${id}`);
    return response.data;
};
export const deleteVehicle = async (id) => {

    await api.delete(`/vehicles/${id}`);
};
export const searchVehicles = async (params) => {

    const response = await api.get(
        "/vehicles/search",
        {
            params,
        }
    );

    return response.data;
};
export async function purchaseVehicle(id, quantity) {
    const response = await api.post(
        `/vehicles/${id}/purchase`,
        null,
        {
            params: { quantity }
        }
    );

    return response.data;
}