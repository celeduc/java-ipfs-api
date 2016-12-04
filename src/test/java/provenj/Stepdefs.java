package provenj;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.PendingException;
import org.json.simple.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.UUID;
import static org.junit.Assert.assertEquals;

public class Stepdefs {

    // Build manifest
    Manifest manifest = new Manifest();
    String m_fileName;

    @Given("^a JPEG file named \"([^\"]*)\"$")
    public void a_JPEG_file_named(String fileName) throws Throwable {
        manifest.addFile(fileName);
        m_fileName = fileName;
    }

    @Given("^the current Bitcoin block number (\\d+)$")
    public void the_current_Bitcoin_block_number(int blockNumber) throws Throwable {
        manifest.setBitcoinBlockNumber(blockNumber);
    }

    @Given("^the current Bitcoin block hash \"([^\"]*)\"$")
    public void the_current_Bitcoin_block_hash(String blockHash) throws Throwable {
        manifest.setBitcoinBlockHash(blockHash);
    }

    @Given("^the current Ethereum block number (\\d+)$")
    public void the_current_Ethereum_block_number(int blockNumber) throws Throwable {
        manifest.setEthereumBlockNumber(blockNumber);
    }

    @Given("^the current Ethereum block hash \"([^\"]*)\"$")
    public void the_current_Ethereum_block_hash(String blockHash) throws Throwable {
        manifest.setEthereumBlockHash(blockHash);
    }

    @Given("^the IPFS hash from the last submitted file \"([^\"]*)\"$")
    public void the_IPFS_hash_from_the_last_submitted_file(String ipfsHash) throws Throwable {
        manifest.setPreviousIPFSHash(ipfsHash);
    }

    @Given("^the hashes for the last submitted file \"([^\"]*)\"$")
    public void the_hashes_for_the_last_submitted_file(String fileHashes) throws Throwable {
        manifest.setPreviousFileHashes(fileHashes);
    }

    @Given("^the hashes for the file \"([^\"]*)\"$")
    public void the_hashes_for_the_file(String fileHashes) throws Throwable {
        manifest.setFileHashes(fileHashes);
    }

    @Given("^the other hashes for the file \"([^\"]*)\"$")
    public void the_other_hashes_for_the_file(String fileHashes) throws Throwable {
        manifest.setFileHashes(fileHashes);
    }

    @Given("^the GUID for the submission \"([^\"]*)\"$")
    public void the_GUID_for_the_submission(String guid) throws Throwable {
        manifest.setGUID(UUID.fromString(guid));
    }

    @When("^I ask for a manifest file$")
    public void i_ask_for_a_manifest_file() throws Throwable {
        JSONObject json = manifest.get();
    }

    @Then("^manifest\\.FileName should equal \"([^\"]*)\"$")
    public void manifest_FileName_should_equal(String filename) throws Throwable {
        JSONObject json = manifest.get();
        assertEquals(filename,json.get(ProvenLib.PROVEN_FILE_NAME));
    }

    @Then("^manifest\\.BitcoinBlockNumber should be (\\d+)$")
    public void manifest_BitcoinBlockNumber_should_be(int bitcoinBlockNumber) throws Throwable {
        JSONObject json = manifest.get();
        assertEquals(bitcoinBlockNumber,(int)json.get(ProvenLib.PROVEN_BITCOIN_BLOCK_NUMBER));
    }

    @Then("^manifest\\.BitcoinBlockHash should equal \"([^\"]*)\"$")
    public void manifest_BitcoinBlockHash_should_equal(String hash) throws Throwable {
        JSONObject json = manifest.get();
        assertEquals(hash,json.get(ProvenLib.PROVEN_BITCOIN_BLOCK_HASH));
    }

    @Then("^manifest\\.EthereumBlockNumber should equal (\\d+)$")
    public void manifest_EthereumBlockNumber_should_equal(int num) throws Throwable {
        JSONObject json = manifest.get();
        assertEquals(num, (int)json.get(ProvenLib.PROVEN_ETHEREUM_BLOCK_NUMBER));
    }

    @Then("^manifest\\.EthereumBlockHash should equal \"([^\"]*)\"$")
    public void manifest_EthereumBlockHash_should_equal(String hash) throws Throwable {
        JSONObject json = manifest.get();
        assertEquals(hash,json.get(ProvenLib.PROVEN_ETHEREUM_BLOCK_HASH));
    }

    @Then("^manifest\\.PreviousIFPSHash should equal \"([^\"]*)\"$")
    public void manifest_PreviousIFPSHash_should_equal(String hash) throws Throwable {
        JSONObject json = manifest.get();
        assertEquals(hash, json.get(ProvenLib.PROVEN_PREVIOUS_IPFS_HASH));
    }

    @Then("^manifest\\.PreviousFileHashes should equal \"([^\"]*)\"$")
    public void manifest_PreviousFileHashes_should_equal(String hashes) throws Throwable {
        JSONObject json = manifest.get();
        assertEquals(hashes, json.get(ProvenLib.PROVEN_PREVIOUS_FILE_HASHES));
    }

    @Then("^manifest\\.FileHashes should equal \"([^\"]*)\"$")
    public void manifest_FileHashes_should_equal(String hashes) throws Throwable {
        JSONObject json = manifest.get();
        assertEquals(hashes, json.get(ProvenLib.PROVEN_FILE_HASHES));
    }

    @Then("^manifest\\.GUID should equal \"([^\"]*)\"$")
    public void manifest_GUID_should_equal(String guid) throws Throwable {
        JSONObject json = manifest.get();
        assertEquals(guid, json.get(ProvenLib.PROVEN_GUID));
    }

    // Apply Exif to JPEG
    ImageTags imageTags = null;
    String outputFileName = "";

    @Given("^a JPEG file \"([^\"]*)\"$")
    public void a_JPEG_file(String fileName) throws Throwable {
        FileInputStream inputFile = new FileInputStream(fileName);
        File tempFile = File.createTempFile("provenj", ".jpeg");
        tempFile.deleteOnExit();
        outputFileName = tempFile.getCanonicalPath();
        FileOutputStream outputFile = new FileOutputStream(tempFile.getCanonicalFile());

        imageTags = new ImageTags(inputFile,outputFile);
    }

    @Given("^the Bitcoin block number (\\d+)$")
    public void the_Bitcoin_block_number(int blockNumber) throws Throwable {
        imageTags.setBitcoinBlockNumber(blockNumber);
    }

    @Given("^the Bitcoin block hash \"([^\"]*)\"$")
    public void the_Bitcoin_block_hash(String blockHash) throws Throwable {
        imageTags.setBitcoinBlockHash(blockHash);
    }

    @Given("^the Ethereum block number (\\d+)$")
    public void the_Ethereum_block_number(int blockNumber) throws Throwable {
        imageTags.setEthereumBlockNumber(blockNumber);
    }

    @Given("^the Ethereum block hash \"([^\"]*)\"$")
    public void the_Ethereum_block_hash(String blockHash) throws Throwable {
        imageTags.setEthereumBlockHash(blockHash);
    }

    @Given("^the IPFS hash from the last file \"([^\"]*)\"$")
    public void the_IPFS_hash_from_the_last_file(String ipfsHash) throws Throwable {
        imageTags.setPreviousIPFSHash(ipfsHash);
    }

    @Given("^the other hashes from the last file \"([^\"]*)\"$")
    public void the_other_hashes_from_the_last_file(String otherHashes) throws Throwable {
        imageTags.setPreviousFileHashes(otherHashes);
    }

    @Given("^the GUID \"([^\"]*)\"$")
    public void the_GUID(String guid) throws Throwable {
        imageTags.setGUID(UUID.fromString(guid));
    }

    private String getTag(String tagName) {
        Runtime rt = Runtime.getRuntime();
        String command = String.format("exiftool -xmp:%1$s -a -b %2$s", tagName, outputFileName);

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

    @When("^I load the data from the JPEG file returned$")
    public void i_load_the_data_from_the_JPEG_file_returned() throws Throwable {
        FileOutputStream outputFile = imageTags.getFile();
        outputFile.close();
    }

    @Then("^Exif\\.BitcoinBlockNumber should match (\\d+)$")
    public void exif_BitcoinBlockNumber_should_match(int blockNumber) throws Throwable {
        assertEquals(Integer.toString(blockNumber), getTag(ProvenLib.PROVEN_BITCOIN_BLOCK_NUMBER));
    }

    @Then("^Exif\\.BitcoinLastBlockHash should equal \"([^\"]*)\"$")
    public void exif_BitcoinLastBlockHash_should_equal(String blockHash) throws Throwable {
        assertEquals(blockHash, getTag(ProvenLib.PROVEN_BITCOIN_BLOCK_HASH));
    }

    @Then("^Exif\\.EthereumBlockNumber should equal (\\d+)$")
    public void exif_EthereumBlockNumber_should_equal(int blockNumber) throws Throwable {
        assertEquals(Integer.toString(blockNumber), getTag(ProvenLib.PROVEN_ETHEREUM_BLOCK_NUMBER));
    }

    @Then("^Exif\\.EthereumLastBlockHash should equal \"([^\"]*)\"$")
    public void exif_EthereumLastBlockHash_should_equal(String blockHash) throws Throwable {
        assertEquals(blockHash, getTag(ProvenLib.PROVEN_ETHEREUM_BLOCK_HASH));
    }

    @Then("^Exif\\.ProvenPrevIFPSHandle should equal \"([^\"]*)\"$")
    public void exif_ProvenPrevIFPSHandle_should_equal(String ipfsHash) throws Throwable {
        assertEquals(ipfsHash, getTag(ProvenLib.PROVEN_PREVIOUS_IPFS_HASH));
    }

    @Then("^Exif\\.ProvenFileHashes should equal \"([^\"]*)\"$")
    public void exif_ProvenFileHashes_should_equal(String hashes) throws Throwable {
        assertEquals(hashes, getTag(ProvenLib.PROVEN_PREVIOUS_FILE_HASHES));
    }

    @Then("^Exif\\.ProvenGUID should equal \"([^\"]*)\"$")
    public void exif_ProvenGUID_should_equal(String guid) throws Throwable {
        assertEquals(guid, getTag(ProvenLib.PROVEN_GUID));
    }

    IndexCreator indexCreator;
    String index;


    @When("^I create an index$")
    public void i_create_an_index() throws Throwable {
        indexCreator = new IndexCreator(manifest);
        index = indexCreator.toString();
        assert(index.matches("^<html>.*</html>$"));
    }

    @Then("^the output file should list the file name$")
    public void the_output_file_should_list_the_file_name() throws Throwable {
        assert(index.matches(String.format("^.*%s.*$", m_fileName)));
    }

    @Then("^the output file should have a static link to the file$")
    public void the_output_file_should_have_a_static_link_to_the_file() throws Throwable {
        assert(index.matches(String.format("^.*a href.*%s.*$",m_fileName)));
    }

    @Then("^the output file should include the last Ethereum hash$")
    public void the_output_file_should_include_the_hash_information_for_the_file() throws Throwable {
        assert(index.matches(String.format("^.*%s.*$",manifest.get().get("EthereumBlockHash"))));
    }
}
