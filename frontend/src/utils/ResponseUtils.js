import { parseLinkHeader } from '@web3-storage/parse-link-header';

export const parsePaginatedResponse = (response) => {
    if (!response || !response.data) {
        return { links: null, data: null };
    }
    const linkHeader = response.headers?.link;
    const links = linkHeader ? parseLinkHeader(linkHeader) : null;
    const data = response.data;
    const status = response.status
    return { links, data , status};
};
