package provenj;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

public class Stepdefs {

    Metadata metadata = new Metadata();

    @Given("^the Bitcoin block number (\\d+)$")
    public void the_Bitcoin_block_number(int blockNumber) throws Throwable {
        metadata.setBitcoinBlockNumber(blockNumber);
    }

    @Given("^the Bitcoin block hash \"([^\"]*)\"$")
    public void the_Bitcoin_block_hash(String blockHash) throws Throwable {
        metadata.setBitcoinBlockHash(blockHash);
    }

    @Given("^the Ethereum block number (\\d+)$")
    public void the_Ethereum_block_number(int blockNumber) throws Throwable {
        metadata.setEthereumBlockNumber(blockNumber);
    }

    @Given("^the Ethereum block hash \"([^\"]*)\"$")
    public void the_Ethereum_block_hash(String blockHash) throws Throwable {
        metadata.setEthereumBlockHash(blockHash);
    }

    @Given("^the IPFS hash from the last file \"([^\"]*)\"$")
    public void the_IPFS_hash_from_the_last_file(String ipfsHash) throws Throwable {
        metadata.setPreviousIPFSHash(ipfsHash);
    }

    @Given("^the hashes from the last file \"([^\"]*)\"$")
    public void the_hashes_from_the_last_file(String previousFileHashes) throws Throwable {
        metadata.setPreviousFileHashes(previousFileHashes);
    }

    @Given("^the GUID \"([^\"]*)\"$")
    public void the_GUID(String guid) throws Throwable {
        metadata.setGUID(UUID.fromString(guid));
    }

    // Read XMP tag from JPEG file using exiftool for testing purposes.
    private String getTag(String tagName, Path filePath) {
        Runtime rt = Runtime.getRuntime();
        String command = String.format("exiftool -xmp:%1$s -a -b %2$s", tagName, filePath.toString());

        try {
            Process proc = rt.exec(command);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));

            return stdInput.readLine();
        }
        catch (Exception e) {
            return "";
        }
    }

    // Test enclosure creation
    Enclosure enclosure = null;

    @When("^I provide a JPEG file \"([^\"]*)\"$")
    public void i_provide_a_jpeg_file(String inputFilePath) throws Throwable {
        // Get file name
        metadata.setFileName(Paths.get(inputFilePath).getFileName().toString());

        enclosure = new Enclosure();
        enclosure.fillEnclosure(Paths.get(inputFilePath),metadata);
    }

    @Then("^there should exist a directory$")
    public void there_should_exist_a_directory() throws Throwable {
        assert(Files.isDirectory(enclosure.getPath()));
    }

    @Then("^it should contain a manifest$")
    public void it_should_contain_a_manifest() throws Throwable {
        assert(Files.exists(enclosure.getPath(ProvenLib.PROVEN_MANIFEST)));
    }

    @Then("^it should contain an index$")
    public void it_should_contain_an_index() throws Throwable {
        assert(Files.exists(enclosure.getPath(ProvenLib.PROVEN_INDEX)));
    }

    @Then("^it should contain in the payload directory the file \"([^\"]*)\"$")
    public void it_should_contain_in_the_payload_directory_the_file(String fileName) throws Throwable {
        assert(Files.exists(Paths.get(enclosure.getPath(ProvenLib.PROVEN_CONTENT_DIRECTORY).toString(),
                                      fileName)));
        assertEquals(fileName, metadata.getFileName());
    }

    // For testing, check a tag in the image in the enclosure.
    protected String getFinalImageTag(String tag){
        return getTag(tag, Paths.get(enclosure.getPath(ProvenLib.PROVEN_CONTENT_DIRECTORY).toString(),
                                     metadata.getFileName()));
    }

    @Then("^the image should contain the Ethereum block number (\\d+)$")
    public void the_image_should_contain_the_Ethereum_block_number(int blockNumber) throws Throwable {
        assertEquals(Integer.toString(blockNumber),
                     getFinalImageTag(ProvenLib.PROVEN_ETHEREUM_BLOCK_NUMBER));
        assertEquals(metadata.getEthereumBlockNumber(), blockNumber);
    }

    JSONObject finalJson = null;

    @Then("^the manifest\\.GUID should equal \"([^\"]*)\"$")
    public void the_manifest_GUID_should_equal(String guid) throws Throwable {
        String json = new String(Files.readAllBytes(enclosure.getPath(ProvenLib.PROVEN_MANIFEST)));
        JSONParser parser = new JSONParser();
        finalJson = (JSONObject) parser.parse(json);
        assertEquals(guid, finalJson.get(ProvenLib.PROVEN_GUID));
        assertEquals(guid, metadata.getGUID().toString());
    }

    @Then("^the File Hashes of the image should match the File Hashes in the manifest$")
    public void the_File_Hashes_of_the_image_should_match_the_File_Hashes_in_the_manifest() throws Throwable {
        assertEquals(finalJson.get(ProvenLib.PROVEN_FILE_HASHES),
                     Enclosure.calculateFileHash(Paths.get(enclosure.getPath(ProvenLib.PROVEN_CONTENT_DIRECTORY).toString(),
                                                           finalJson.get(ProvenLib.PROVEN_FILE_NAME).toString())));
        assertEquals(finalJson.get(ProvenLib.PROVEN_FILE_HASHES), metadata.getFileHashes());
        // NOTE: the file hash OF the image can't be IN the image.
    }

    @Then("^the Bitcoin block hash everywhere is \"([^\"]*)\"$")
    public void the_Bitcoin_block_hash_everywhere_is(String blockHash) throws Throwable {
        assertEquals(blockHash, finalJson.get(ProvenLib.PROVEN_BITCOIN_BLOCK_HASH));
        assertEquals(blockHash, getFinalImageTag(ProvenLib.PROVEN_BITCOIN_BLOCK_HASH));
    }

    @Then("^the Bitcoin block number everywhere is (\\d+)$")
    public void the_Bitcoin_block_number_everywhere_is(int blockNumber) throws Throwable {
        assertEquals(Integer.toString(blockNumber), finalJson.get(ProvenLib.PROVEN_BITCOIN_BLOCK_NUMBER).toString());
        assertEquals(Integer.toString(blockNumber), getFinalImageTag(ProvenLib.PROVEN_BITCOIN_BLOCK_NUMBER));
    }

    @Then("^the Ethereum block hash everywhere is \"([^\"]*)\"$")
    public void the_Ethereum_block_hash_everywhere_is(String blockHash) throws Throwable {
        assertEquals(blockHash, finalJson.get(ProvenLib.PROVEN_ETHEREUM_BLOCK_HASH));
        assertEquals(blockHash, getFinalImageTag(ProvenLib.PROVEN_ETHEREUM_BLOCK_HASH));
    }

    @Then("^the Ethereum block number everywhere is (\\d+)$")
    public void the_Ethereum_block_number_everywhere_is(int blockNumber) throws Throwable {
        assertEquals(Integer.toString(blockNumber), finalJson.get(ProvenLib.PROVEN_ETHEREUM_BLOCK_NUMBER).toString());
        assertEquals(Integer.toString(blockNumber), getFinalImageTag(ProvenLib.PROVEN_ETHEREUM_BLOCK_NUMBER));
    }

    @Then("^the last IPFS file hash everywhere is \"([^\"]*)\"$")
    public void the_last_IPFS_file_hash_everywhere_is(String ipfsHash) throws Throwable {
        assertEquals(ipfsHash, finalJson.get(ProvenLib.PROVEN_PREVIOUS_IPFS_HASH));
        assertEquals(ipfsHash, getFinalImageTag(ProvenLib.PROVEN_PREVIOUS_IPFS_HASH));
    }

    @Then("^the last file hashes everywhere is \"([^\"]*)\"$")
    public void the_last_file_hashes_everywhere_is(String fileHashes) throws Throwable {
        assertEquals(fileHashes, finalJson.get(ProvenLib.PROVEN_PREVIOUS_FILE_HASHES));
        assertEquals(fileHashes, getFinalImageTag(ProvenLib.PROVEN_PREVIOUS_FILE_HASHES));
    }
}
