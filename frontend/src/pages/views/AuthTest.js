import React, { useState } from 'react';
import userApi from '../../api/UserApi';

function AuthTest() {
    const [authStatus, setAuthStatus] = useState(null);

    const handleAuthTest = async () => {
        const isAuthenticated = await userApi.authTest();
        setAuthStatus(isAuthenticated ? 'Authenticated' : 'Not Authenticated');
    };

    return (
        <div className="auth-test">
            <button onClick={handleAuthTest}>Test Authentication</button>
            {authStatus !== null && (
                <div>
                    {authStatus === 'Authenticated' ? (
                        <p>You are authenticated!</p>
                    ) : (
                        <p>You are not authenticated.</p>
                    )}
                </div>
            )}
        </div>
    );
}

export default AuthTest;
