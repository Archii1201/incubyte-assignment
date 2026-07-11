import { render, screen } from "@testing-library/react";
import AddVehicleForm from "../components/AddVehicleForm";

test("renders add vehicle form", () => {

    render(<AddVehicleForm />);

    expect(
        screen.getByPlaceholderText("Make")
    ).toBeInTheDocument();

    expect(
        screen.getByPlaceholderText("Model")
    ).toBeInTheDocument();

    expect(
        screen.getByPlaceholderText("Category")
    ).toBeInTheDocument();

    expect(
        screen.getByPlaceholderText("Price")
    ).toBeInTheDocument();

    expect(
        screen.getByPlaceholderText("Quantity")
    ).toBeInTheDocument();

    expect(
        screen.getByRole("button", {
            name: /add vehicle/i
        })
    ).toBeInTheDocument();

});