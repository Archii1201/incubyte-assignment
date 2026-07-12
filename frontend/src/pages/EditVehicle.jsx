import EditVehicleForm from "../components/EditVehicleForm";
import Layout from "../components/Layout";
export default function EditVehicle() {

    return (
        <Layout>
        <div className="container">

            <h2>Edit Vehicle</h2>

            <EditVehicleForm />

        </div>
        </Layout>

    );
}